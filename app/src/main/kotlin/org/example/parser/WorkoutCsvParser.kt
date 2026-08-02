package org.example.parser

import org.example.model.WorkoutRow
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object WorkoutCsvParser {
    private val locale = Locale.forLanguageTag("pt-BR")
    private val dateFormatters = listOf(
        DateTimeFormatter.ofPattern("d 'de' MMM. 'de' yyyy, HH:mm", locale),
        DateTimeFormatter.ofPattern("d 'de' MMM 'de' yyyy, HH:mm", locale)
    )

    fun parse(inputPath: Path): List<WorkoutRow> {
        val records = CsvParser.parse(inputPath)
        if (records.isEmpty()) return emptyList()

        val header = records.first().map { it.trim() }
        val titleIndex = header.indexOf("title")
        val startTimeIndex = header.indexOf("start_time")
        val exerciseTitleIndex = header.indexOf("exercise_title")
        val weightIndex = header.indexOf("weight_kg")
        val repsIndex = header.indexOf("reps")

        if (titleIndex < 0 || startTimeIndex < 0 || exerciseTitleIndex < 0) {
            throw IllegalArgumentException("CSV de treino não contém colunas obrigatórias: title, start_time, exercise_title")
        }

        return records.drop(1).mapNotNull { record ->
            if (record.size <= maxOf(titleIndex, startTimeIndex, exerciseTitleIndex)) {
                return@mapNotNull null
            }

            val title = record[titleIndex].trim()
            val startTimeText = record[startTimeIndex].trim()
            val exerciseTitle = record[exerciseTitleIndex].trim()
            if (title.isEmpty() || startTimeText.isEmpty() || exerciseTitle.isEmpty()) {
                println("Skipping row because required field empty: title='$title', startTime='$startTimeText', exerciseTitle='$exerciseTitle'")
                return@mapNotNull null
            }

            val startTime = parseDateTime(startTimeText)
            if (startTime == null) {
                println("Skipping row because date parse failed: '$startTimeText'")
                return@mapNotNull null
            }
            val weightKg = record.getOrNull(weightIndex)?.trim()?.takeIf { it.isNotBlank() }?.toDoubleOrNull()
            val reps = record.getOrNull(repsIndex)?.trim()?.takeIf { it.isNotBlank() }?.toIntOrNull()

            WorkoutRow(
                title = title,
                startTime = startTime,
                exerciseTitle = exerciseTitle,
                weightKg = weightKg,
                reps = reps
            )
        }
    }

    private fun parseDateTime(value: String): LocalDateTime? {
        return dateFormatters.firstNotNullOfOrNull { formatter ->
            try {
                LocalDateTime.parse(value, formatter)
            } catch (exception: DateTimeParseException) {
                null
            }
        }
    }
}
