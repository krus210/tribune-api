package ru.korolevss.repository

import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel

interface PostRepository {
    suspend fun getById(postId: Long): PostModel?
    suspend fun getAll(): List<PostModel>
    suspend fun getRecentPosts(): List<PostModel>
    suspend fun getPostsBefore(postId: Long): List<PostModel>?
    suspend fun save(post: PostModel): PostModel
    suspend fun removeById(postId: Long)
    suspend fun likeById(postId: Long, userId: Long): PostModel?
    suspend fun dislikeById(postId: Long, userId: Long): PostModel?
    suspend fun getUserPosts(userId: Long): List<PostModel>
}
