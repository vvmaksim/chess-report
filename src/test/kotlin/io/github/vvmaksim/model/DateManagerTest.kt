package io.github.vvmaksim.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateManagerTest {
    @Test
    fun `getFormattedDateAsString should return date in yyyy-MM-dd format`() {
        val inputDate = "11.12.2025"
        val expectedDate = "2025-12-11"
        val actualDate = DateManager.getFormattedDateAsString(inputDate)
        assertEquals(expectedDate, actualDate)
    }

    @Test
    fun `getCurrentDateAsString should return actual date in dd{dot}MM{dot}yyyy format`() {
        val expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val actualDate = DateManager.getCurrentDateAsString()
        assertEquals(expectedDate, actualDate)
    }
}
