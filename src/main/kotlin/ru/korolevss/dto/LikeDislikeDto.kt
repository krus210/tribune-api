package ru.korolevss.dto

import io.ktor.util.KtorExperimentalAPI
import ru.korolevss.model.LikeDislike
import ru.korolevss.model.LikeDislikeModel
import ru.korolevss.model.UserStatus
import ru.korolevss.service.UserService
import java.time.format.DateTimeFormatter

data class LikeDislikeDto(
        val date: String,
        val userId: Long,
        val username: String,
        val status: UserStatus,
        val likeDislike: LikeDislike,
        val attachmentImage: String?
) {
    companion object {
        @KtorExperimentalAPI
        suspend fun fromModel(model: LikeDislikeModel, userService: UserService): LikeDislikeDto {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY")
            val date = model.date.format(formatter)


            return LikeDislikeDto(
                    date = date,
                    userId = model.user.id,
                    username = model.user.name,
                    status = userService.checkStatus(model.user.id),
                    likeDislike = model.likeDislike,
                    attachmentImage = model.user.attachmentImage
            )
        }
    }
}