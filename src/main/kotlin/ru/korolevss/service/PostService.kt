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
    suspend fun getById(postId: Long, userId: Long, userService: UserService): PostResponseDto {
        val post = repo.getById(postId) ?: throw NotFoundException()
        return PostResponseDto.fromModel(post, userId, userService)
    }

    @KtorExperimentalAPI
    suspend fun getPostById(postId: Long) = repo.getById(postId) ?: throw NotFoundException()


    @KtorExperimentalAPI
    suspend fun getRecentPosts(userId: Long, userService: UserService) =
            repo.getRecentPosts().map { PostResponseDto.fromModel(it, userId, userService) }

    @KtorExperimentalAPI
    suspend fun getPostsBefore(userId: Long, postId: Long, userService: UserService): List<PostResponseDto>  {
        val listPostsAfter = repo.getPostsBefore(postId) ?: throw NotFoundException()
        return listPostsAfter.map { PostResponseDto.fromModel(it, userId, userService) }
    }

    @KtorExperimentalAPI
    suspend fun save(userId: Long, post: PostRequestDto, userService: UserService){
        if (userService.checkReadOnly(userId, this)) throw UserAccessException("Read Only mode")
        val date = LocalDateTime.now()
        val newPost = PostModel(
                id = 0L,
                userId = userId,
                date = date,
                text = post.text,
                attachmentImage = post.attachmentImage,
                attachmentLink = post.attachmentLink
        )
        userService.addPostId(userId, repo.save(newPost).id)
    }

    @KtorExperimentalAPI
    suspend fun removeById(userId: Long, postId: Long, userService: UserService) {
        if (userService.checkReadOnly(userId, this)) throw UserAccessException("Read Only mode")
        val post = repo.getById(postId) ?: throw NotFoundException()
        if (post.userId == userId) {
            repo.removeById(postId)
            userService.removePostId(userId, postId)
        } else {
            throw UserAccessException("You didn't create this post")
        }
    }

    @KtorExperimentalAPI
    suspend fun likeById(userId: Long, postId: Long, userService: UserService, fcmService: FCMService): PostResponseDto {
        val post = repo.likeById(postId, userId) ?: throw NotFoundException()
        val userOfPost = userService.getById(post.userId)
        val user = userService.getById(userId)
        if (!userOfPost.token.isNullOrEmpty()) {
            fcmService.send(userOfPost.id, userOfPost.token!!, "Your post liked by ${user.name}")
        }
        val postResponseDto = PostResponseDto.fromModel(post, userId, userService)
        userService.addLike(userId)
        return postResponseDto
    }

    @KtorExperimentalAPI
    suspend fun dislikeById(userId: Long, postId: Long, userService: UserService, fcmService: FCMService): PostResponseDto {
        val post = repo.dislikeById(postId, userId) ?: throw NotFoundException()
        val userOfPost = userService.getById(post.userId)
        val user = userService.getById(userId)
        if (!userOfPost.token.isNullOrEmpty()) {
            fcmService.send(userOfPost.id, userOfPost.token!!, "Your post disliked by ${user.name}")
        }
        val postResponseDto = PostResponseDto.fromModel(post, userId, userService)
        userService.addDislike(userId)
        return postResponseDto
    }

    @KtorExperimentalAPI
    suspend fun getUserPosts(userId: Long, userService: UserService) =
            repo.getUserPosts(userId).map { PostResponseDto.fromModel(it, userId, userService) }


}
