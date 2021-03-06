package ru.korolevss.model

import java.time.LocalDateTime

data class LikeDislikeModel(val date: LocalDateTime, val user: UserModel, val likeDislike: LikeDislike)

enum class LikeDislike {
    LIKE,
    DISLIKE
}