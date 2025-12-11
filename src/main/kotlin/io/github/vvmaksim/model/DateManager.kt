package io.github.vvmaksim.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateManager {
    fun getCurrentDateAsString(): String {
        val currentDate = LocalDate.now()
        return currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    fun getFormattedDateAsString(date: String): String {
        val date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}
