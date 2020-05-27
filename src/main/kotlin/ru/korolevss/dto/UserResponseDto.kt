package ru.korolevss.dto

import io.ktor.util.KtorExperimentalAPI
import ru.korolevss.model.UserStatus
import ru.korolevss.service.PostService
import ru.korolevss.service.UserService


data class UserResponseDto(
        val id: Long,
        val name: String,
        val attachmentImage: String?,
        val status: UserStatus,
        val token: String?,
        val readOnly: Boolean = false
) {
    companion object {
        @KtorExperimentalAPI
        suspend fun fromModel(userId: Long, userService: UserService, postService: PostService): UserResponseDto {
            val user = userService.getById(userId)
            return UserResponseDto(
                    id = user.id,
                    name = user.name,
                    attachmentImage = user.attachmentImage,
                    status = userService.checkStatus(user.id),
                    token = user.token,
                    readOnly = userService.checkReadOnly(userId, postService)
            )
        }
    }
}

