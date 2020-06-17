package ru.korolevss.repository

import ru.korolevss.model.*
import ru.korolevss.service.PostService

interface UserRepository {
    suspend fun getById(userId: Long): UserModel?
    suspend fun getByUsername(username: String): UserModel?
    suspend fun save(item: UserModel): UserModel
    suspend fun saveFirebaseToken(userId: Long, firebaseToken: String): UserModel?
    suspend fun addLike(userId: Long): UserModel?
    suspend fun addDislike(userId: Long): UserModel?
    suspend fun checkReadOnly(userId: Long, postService: PostService): Boolean
    suspend fun checkStatus(user: UserModel): UserStatus
    suspend fun update(userId: Long, newPassword: String)
    suspend fun addPostId(user: UserModel, postId: Long)
    suspend fun removePostId(user: UserModel, postId: Long)
    suspend fun listUsersLikeDislikePostById(post: PostModel): List<LikeDislikeModel>
    suspend fun getByIdPassword(id: Long, password: String): UserModel?
    suspend fun addImage(user: UserModel, mediaModel: MediaModel)

}