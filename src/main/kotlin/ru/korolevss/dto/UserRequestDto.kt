package ru.korolevss.dto

data class UserRequestDto(
        val name: String,
        val password: String,
        val attachmentImage: String? = null
)