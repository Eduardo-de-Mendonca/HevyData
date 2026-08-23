package org.example.parser

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object DateTimeParser {
    private val months = mapOf(
        "Jan" to 1,
        "Fev" to 2,
        "Mar" to 3,
        "Abr" to 4,
        "Mai" to 5,
        "Jun" to 6,
        "Jul" to 7,
        "Ago" to 8,
        "Set" to 9,
        "Out" to 10,
        "Nov" to 11,
        "Dez" to 12,
    )
    
    fun parseDateTime(value: String): LocalDateTime? {
        val partsDateTime = value.split(", ")
        val datePart = partsDateTime.getOrNull(0)
        val timePart = partsDateTime.getOrNull(1)

        val dateParts = datePart?.split(" ")
        val day = dateParts?.getOrNull(0)?.toIntOrNull()
        val month = months[dateParts?.getOrNull(1)]
        val year = dateParts?.getOrNull(2)?.toIntOrNull()

        val timeParts = timePart?.split(":")
        val hour = timeParts?.getOrNull(0)?.toIntOrNull()
        val minute = timeParts?.getOrNull(2)?.toIntOrNull()

        if (day == null || month == null || year == null || hour == null || minute == null) {
            return null
        }
        val date = LocalDate.of(year, day, month)
        val time = LocalTime.of(hour, minute)
        return LocalDateTime.of(year, month, day, hour, minute)
    }
}