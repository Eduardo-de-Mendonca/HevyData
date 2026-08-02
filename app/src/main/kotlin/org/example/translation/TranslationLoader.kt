package org.example.translation

import org.example.parser.CsvParser
import java.nio.file.Files
import java.nio.file.Path

object TranslationLoader {
    fun load(path: Path): Map<String, String> {
        if (Files.notExists(path)) {
            throw IllegalArgumentException("Arquivo de traduções não encontrado: $path")
        }

        val records = CsvParser.parse(path)
        if (records.isEmpty()) return emptyMap()

        val header = records.first().map { it.trim() }
        val exerciseTitleIndex = header.indexOf("exercise_title")
        val translationIndex = header.indexOf("translation")

        if (exerciseTitleIndex < 0 || translationIndex < 0) {
            throw IllegalArgumentException("CSV de traduções precisa conter as colunas exercise_title e translation")
        }

        return records.drop(1).mapNotNull { record ->
            if (record.size <= maxOf(exerciseTitleIndex, translationIndex)) return@mapNotNull null
            val exerciseTitle = record[exerciseTitleIndex].trim()
            val translation = record[translationIndex].trim()
            if (exerciseTitle.isEmpty() || translation.isEmpty()) return@mapNotNull null
            exerciseTitle to translation
        }.toMap()
    }
}
