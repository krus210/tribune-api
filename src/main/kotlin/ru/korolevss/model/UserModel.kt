package ru.korolevss.model

import io.ktor.auth.Principal

data class UserModel(
        val id: Long,
        val name: String,
        var password: String,
        var userPostsId: MutableList<Long> = mutableListOf(),
        val attachmentImage: String? = null,
        var token: String? = null,
        var status: UserStatus = UserStatus.NORMAL,
        var readOnly: Boolean = false,
        var likes: Long = 0,
        var dislikes: Long = 0
): Principal

enum class UserStatus {
    NORMAL,
    PROMOTER,
    HATER
}
