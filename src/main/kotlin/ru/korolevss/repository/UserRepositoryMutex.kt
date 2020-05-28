package ru.korolevss.repository

import io.ktor.util.KtorExperimentalAPI
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.korolevss.model.*
import ru.korolevss.service.PostService
import ru.korolevss.users

class UserRepositoryMutex : UserRepository {
    private var nextId = atomic(11L)
    private val items = users
    private val mutex = Mutex()

    override suspend fun getByIdPassword(id: Long, password: String): UserModel? {
        val user = items.find { it.id == id }
        return if (password == user?.password) {
            user
        } else {
            null
        }
    }

    override suspend fun getById(userId: Long): UserModel? = items.find { it.id == userId }

    override suspend fun getByUsername(username: String): UserModel? = items.find { it.name == username }

    override suspend fun save(item: UserModel): UserModel {
        val copy = item.copy(id = nextId.incrementAndGet())
        mutex.withLock {
            items.add(copy)
        }
        return copy
    }

    override suspend fun update(userId: Long, newPassword: String) {
        val index = items.indexOfFirst { it.id == userId}
        mutex.withLock {
            items[index].password = newPassword
        }
        items[index]
    }

    override suspend fun saveFirebaseToken(userId: Long, firebaseToken: String): UserModel? {
        return when (val index = items.indexOfFirst { it.id == userId }) {
            -1 -> {
                null
            }
            else -> {
                mutex.withLock {
                    items[index].token = firebaseToken
                }
                items[index]
            }
        }
    }

    override suspend fun addLike(userId: Long): UserModel? {
        return when (val index = items.indexOfFirst { it.id == userId }) {
            -1 -> {
                null
            }
            else -> {
                mutex.withLock {
                    items[index].likes++
                }
                items[index]
            }
        }
    }

    override suspend fun addDislike(userId: Long): UserModel? {
        return when (val index = items.indexOfFirst { it.id == userId }) {
            -1 -> {
                null
            }
            else -> {
                mutex.withLock {
                    items[index].dislikes++
                }
                items[index]
            }
        }
    }

    @KtorExperimentalAPI
    override suspend fun checkReadOnly(userId: Long, postService: PostService): Boolean {
        val index = items.indexOfFirst { it.id == userId }
        items[index].userPostsId.forEach {
            val post = postService.getPostById(it)
            if (post.dislikeUsersId.size >= 5 && post.likeUsersId.isEmpty()) {         // > 100
                if (!items[index].readOnly) {
                    mutex.withLock {
                        items[index].readOnly = true
                    }
                }
                return true
            } else {
                if (items[index].readOnly) {
                    mutex.withLock {
                        items[index].readOnly = false
                    }
                }
            }
        }
        return items[index].readOnly
    }

    override suspend fun checkStatus(user: UserModel): UserStatus {
        val index = items.indexOfFirst { it.id == user.id }
        val itemsCompareDislikes = items.sortedWith(compareBy { it.dislikes }).reversed()
        val itemsCompareLikes = items.sortedWith(compareBy { it.likes }).reversed()
        val indexUserByDislikes = itemsCompareDislikes.indexOfFirst { it.id == user.id }
        val indexUserByLikes = itemsCompareLikes.indexOfFirst { it.id == user.id }
        if (user.dislikes > 5 || user.dislikes > user.likes * 2 || ((indexUserByDislikes <= 4) && items.size >= 20)) {
            if (user.status != UserStatus.HATER) {
                mutex.withLock {
                    items[index].status = UserStatus.HATER
                }
            }
        } else if (user.likes > 5 || user.likes > user.dislikes * 2 || ((indexUserByLikes <= 4) && items.size >= 20)) {
            if (user.status != UserStatus.PROMOTER) {
                mutex.withLock {
                    items[index].status = UserStatus.PROMOTER
                }
            }
        } else {
            if (user.status != UserStatus.NORMAL) {
                mutex.withLock {
                    items[index].status = UserStatus.NORMAL
                }
            }
        }
        return items[index].status
    }

    override suspend fun addPostId(user: UserModel, postId: Long) {
        val index = items.indexOfFirst { it.id == user.id }
        mutex.withLock {
            items[index].userPostsId.add(postId)
        }
    }

    override suspend fun removePostId(user: UserModel, postId: Long) {
        val index = items.indexOfFirst { it.id == user.id }
        mutex.withLock {
            items[index].userPostsId.remove(postId)
        }
    }

    override suspend fun listUsersLikeDislikePostById(post: PostModel): Map<UserModel, LikeDislikeModel> {
        val mapUsers= mutableMapOf<UserModel, LikeDislikeModel>()
        post.likeUsersId.forEach{
            val user = getById(it.key)
            if (user != null) {
                mapUsers[user] = LikeDislikeModel(it.value, LikeDislike.LIKE)
            }
        }
        post.dislikeUsersId.forEach{
            val user = getById(it.key)
            if (user != null) {
                mapUsers[user] = LikeDislikeModel(it.value, LikeDislike.DISLIKE)
            }
        }
        return mapUsers.toSortedMap(compareByDescending { mapUsers[it]!!.date })
    }
}