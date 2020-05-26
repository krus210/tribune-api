package ru.korolevss.service

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import ru.korolevss.dto.PostRequestDto
import ru.korolevss.dto.PostResponseDto
import ru.korolevss.exception.UserAccessException
import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel
import ru.korolevss.repository.PostRepository
import java.time.LocalDateTime

class PostService(private val repo: PostRepository) {

    @KtorExperimentalAPI
    suspend fun getById(postId: Long, user: UserModel): PostResponseDto {
        val post = repo.getById(postId) ?: throw NotFoundException()
        return PostResponseDto.fromModel(post, user)
    }

    @KtorExperimentalAPI
    suspend fun getPostById(postId: Long) = repo.getById(postId) ?: throw NotFoundException()


    suspend fun getRecentPosts(user: UserModel) =
            repo.getRecentPosts().map { PostResponseDto.fromModel(it, user) }

    @KtorExperimentalAPI
    suspend fun getPostsBefore(user: UserModel, postId: Long): List<PostResponseDto>  {
        val listPostsAfter = repo.getPostsBefore(postId) ?: throw NotFoundException()
        return listPostsAfter.map { PostResponseDto.fromModel(it, user) }
    }

    suspend fun save(user: UserModel, post: PostRequestDto): PostResponseDto {
        if (user.readOnly) throw UserAccessException("Read Only mode")
        val date = LocalDateTime.now()
        val newPost = PostModel(
                id = 0L,
                user = user,
                date = date,
                text = post.text,
                attachmentImage = post.attachmentImage,
                attachmentLink = post.attachmentLink
        )
        return PostResponseDto.fromModel(repo.save(newPost), user)
    }

    @KtorExperimentalAPI
    suspend fun removeById(user: UserModel, postId: Long) {
        if (user.readOnly) throw UserAccessException("Read Only mode")
        val post = repo.getById(postId) ?: throw NotFoundException()
        if (post.user == user) {
            repo.removeById(postId)
        } else {
            throw UserAccessException("You didn't create this post")
        }
    }

    @KtorExperimentalAPI
    suspend fun likeById(user: UserModel, postId: Long, fcmService: FCMService): PostResponseDto {
        val post = repo.likeById(postId, user.id) ?: throw NotFoundException()
        val userOfPost = post.user
        if (!userOfPost.token.isNullOrEmpty()) {
            fcmService.send(userOfPost.id, userOfPost.token!!, "Your post liked by ${user.name}")
        }
        return PostResponseDto.fromModel(post, user)
    }

    @KtorExperimentalAPI
    suspend fun dislikeById(user: UserModel, postId: Long, fcmService: FCMService): PostResponseDto {
        val post = repo.dislikeById(postId, user.id) ?: throw NotFoundException()
        val userOfPost = post.user
        if (!userOfPost.token.isNullOrEmpty()) {
            fcmService.send(userOfPost.id, userOfPost.token!!, "Your post disliked by ${user.name}")
        }
        return PostResponseDto.fromModel(post, user)
    }

    suspend fun getUserPosts(user: UserModel) =
            repo.getUserPosts(user).map { PostResponseDto.fromModel(it, user) }


}
