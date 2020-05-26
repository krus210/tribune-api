package ru.korolevss.dto

import ru.korolevss.model.LikeDislike
import ru.korolevss.model.LikeDislikeModel
import java.time.format.DateTimeFormatter

data class LikeDislikeDto(
        val date: String,
        val likeDislike: LikeDislike
) {
    companion object {
        fun fromModel(model: LikeDislikeModel): LikeDislikeDto {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY")
            val date = model.date.format(formatter)
            return LikeDislikeDto(
                    date = date,
                    likeDislike = model.likeDislike
            )
        }
    }
}