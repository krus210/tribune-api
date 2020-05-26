package ru.korolevss.repository

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel
import ru.korolevss.posts
import java.time.LocalDateTime

class PostRepositoryMutex : PostRepository {

    private var nextId = atomic(25L)
    private val items = posts
    private val mutex = Mutex()

    override suspend fun getById(postId: Long): PostModel? = items.find { it.id == postId }

    override suspend fun getAll() = items.sortedWith(compareBy { it.date }).reversed()

    override suspend fun getRecentPosts(): List<PostModel> {
        try {
            if (items.isEmpty()) {
                return emptyList()
            }
            return getAll().slice(0..20)
        } catch (e: IndexOutOfBoundsException) {
            return getAll()
        }
    }

    override suspend fun getPostsBefore(postId: Long): List<PostModel>? {
        val item = getById(postId)
        return when (val index = getAll().indexOfFirst { it.id == item?.id }) {
            -1 -> null
            (items.size - 1) -> emptyList()
            else -> {
                try {
                    getAll().slice((index + 1)..(index + 20))
                } catch (e: IndexOutOfBoundsException) {
                    getAll().slice((index + 1) until items.size)
                }
            }
        }
    }

    override suspend fun save(post: PostModel): PostModel {
        val copy = post.copy(id = nextId.incrementAndGet())
        mutex.withLock {
            items.add(copy)
        }
        return copy
    }

    override suspend fun removeById(postId: Long) {
        mutex.withLock {
            items.removeIf { it.id == postId }
        }
    }

    override suspend fun likeById(postId: Long, userId: Long): PostModel? {
        val index = items.indexOfFirst { it.id == postId }
        if (index < 0) return null
        val date = LocalDateTime.now()
        mutex.withLock {
            items[index].likeUsersId.put(userId, date)
        }
        return items[index]
    }

    override suspend fun dislikeById(postId: Long, userId: Long): PostModel? {
        val index = items.indexOfFirst { it.id == postId }
        if (index < 0) return null
        val date = LocalDateTime.now()
        mutex.withLock {
            items[index].dislikeUsersId.put(userId, date)
        }
        return items[index]
    }

    override suspend fun getUserPosts(user: UserModel): List<PostModel> =
            items
                .filter {it.user.id == user.id}
                .sortedWith(compareBy { it.date }).reversed()

}