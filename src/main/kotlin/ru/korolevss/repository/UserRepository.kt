package ru.korolevss.repository

import ru.korolevss.model.LikeDislikeModel
import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel
import ru.korolevss.service.PostService

interface UserRepository {
    suspend fun getById(userId: Long): UserModel?
    suspend fun getByUsername(username: String): UserModel?
    suspend fun save(item: UserModel): UserModel
    suspend fun saveFirebaseToken(userId: Long, firebaseToken: String): UserModel?
    suspend fun addLike(userId: Long): UserModel?
    suspend fun addDislike(userId: Long): UserModel?
    suspend fun checkReadOnly(user: UserModel, postService: PostService)
    suspend fun checkStatus(user: UserModel)
    suspend fun checkStatusAllUsers()
    suspend fun addPostId(user: UserModel, postId: Long)
    suspend fun listUsersLikeDislikePostById(post: PostModel): Map<UserModel, LikeDislikeModel>
    suspend fun getByIdPassword(id: Long, password: String): UserModel?
}