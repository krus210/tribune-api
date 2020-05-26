package ru.korolevss.dto

import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel
import ru.korolevss.model.UserStatus
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
        val statusOfUser: UserStatus
) {
    companion object {
        fun fromModel(post: PostModel, user: UserModel): PostResponseDto {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY")
            val date = post.date.format(formatter)
            val likedByUser = post.likeUsersId.contains(user.id)
            val dislikedByUser = post.dislikeUsersId.contains(user.id)
            val isPostOfUser = post.user.id == user.id

            return PostResponseDto(
                    id = post.id,
                    userName = post.user.name,
                    date = date,
                    text = post.text,
                    attachmentImage = post.attachmentImage,
                    attachmentLink = post.attachmentLink,
                    likes = post.likeUsersId.size,
                    dislikes = post.dislikeUsersId.size,
                    likedByUser = likedByUser,
                    dislikedByUser = dislikedByUser,
                    isPostOfUser = isPostOfUser,
                    statusOfUser = user.status
            )
        }
    }
}