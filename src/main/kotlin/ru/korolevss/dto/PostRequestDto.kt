package ru.korolevss.dto

data class PostRequestDto (
        val text: String,
        val attachmentImage: String,
        val attachmentLink: String?
)