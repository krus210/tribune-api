package ru.korolevss.dto

import ru.korolevss.model.UserModel
import ru.korolevss.model.UserStatus


data class UserResponseDto(
        val id: Long,
        val name: String,
        val attachmentImage: String?,
        val status: UserStatus,
        val token: String?,
        val readOnly: Boolean = false
) {
    companion object {
        fun fromModel(user: UserModel) = UserResponseDto(
                id = user.id,
                name = user.name,
                attachmentImage = user.attachmentImage,
                status = user.status,
                token = user.token,
                readOnly = user.readOnly

        )
    }
}

