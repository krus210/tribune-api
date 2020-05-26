package ru.korolevss.dto

import ru.korolevss.model.MediaModel

data class MediaResponseDto(val id: String) {
    companion object {
        fun fromModel(model: MediaModel) = MediaResponseDto(
                id = model.id
        )
    }
}