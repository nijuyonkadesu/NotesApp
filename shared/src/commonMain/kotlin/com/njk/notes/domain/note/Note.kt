package com.njk.notes.domain.note

import com.njk.notes.presentation.*
import kotlinx.datetime.LocalDateTime

data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long, /* using color in shared module, you cannot manage themes platform specific */
    val created: LocalDateTime
) {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, BabyBlueHex, VioletHex, LightGreenHex)
        fun generateColors() = colors.random()
    }
}
