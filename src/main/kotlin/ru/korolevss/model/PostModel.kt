package ru.korolevss.model

import java.time.LocalDateTime


data class PostModel(
        val id: Long,
        val userId: Long,
        val date: LocalDateTime,
        val text: String,
        val attachmentImage: String,
        val attachmentLink: String? = null,
        var likeUsersId: MutableMap<Long, LocalDateTime> = mutableMapOf(),
        var dislikeUsersId: MutableMap<Long, LocalDateTime> = mutableMapOf()
)