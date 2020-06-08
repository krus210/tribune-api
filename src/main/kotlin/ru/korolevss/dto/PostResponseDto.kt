package ru.korolevss.dto

import io.ktor.util.KtorExperimentalAPI
import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel
import ru.korolevss.model.UserStatus
import ru.korolevss.service.UserService
import java.time.format.DateTimeFormatter

data class PostResponseDto(
        val id: Long,
        val userName: String,
        val date: String,
        val text: String,
        val attachmentImage: String,
        val attachmentLink: String?,
        var likes: Int,
        var dislikes: Int,
        val likedByUser: Boolean,
        val dislikedByUser: Boolean,
        val isPostOfUser: Boolean,
        val statusOfUser: UserStatus,
        val attachmentImageUser: String?
) {
    companion object {
        @KtorExperimentalAPI
        suspend fun fromModel(post: PostModel, userId: Long, userService: UserService): PostResponseDto {
            val status = userService.checkStatus(post.userId)
            val user = userService.getById(userId)
            val postUser = userService.getById(post.userId)
            val formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY")
            val date = post.date.format(formatter)
            val likedByUser = post.likeUsersId.contains(user.id)
            val dislikedByUser = post.dislikeUsersId.contains(user.id)
            val isPostOfUser = post.userId == user.id

            return PostResponseDto(
                    id = post.id,
                    userName = postUser.name,
                    date = date,
                    text = post.text,
                    attachmentImage = post.attachmentImage,
                    attachmentLink = post.attachmentLink,
                    likes = post.likeUsersId.size,
                    dislikes = post.dislikeUsersId.size,
                    likedByUser = likedByUser,
                    dislikedByUser = dislikedByUser,
                    isPostOfUser = isPostOfUser,
                    statusOfUser = status,
                    attachmentImageUser = user.attachmentImage
            )
        }
    }
}