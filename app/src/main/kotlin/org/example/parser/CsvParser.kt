package org.example.parser

import java.nio.file.Files
import java.nio.file.Path

object CsvParser {
    fun parse(path: Path): List<List<String>> = parse(Files.readString(path))

    fun parse(content: String): List<List<String>> {
        val records = mutableListOf<List<String>>()
        val currentField = StringBuilder()
        val currentRecord = mutableListOf<String>()
        var inQuotes = false
        var index = 0

        while (index < content.length) {
            val char = content[index]

            when {
                char == '"' -> {
                    if (inQuotes && index + 1 < content.length && content[index + 1] == '"') {
                        currentField.append('"')
                        index += 1
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                char == ',' && !inQuotes -> {
                    currentRecord.add(currentField.toString())
                    currentField.clear()
                }
                (char == '\n' || char == '\r') && !inQuotes -> {
                    if (char == '\r' && index + 1 < content.length && content[index + 1] == '\n') {
                        index += 1
                    }
                    currentRecord.add(currentField.toString())
                    currentField.clear()
                    records.add(currentRecord.toList())
                    currentRecord.clear()
                }
                else -> currentField.append(char)
            }

            index += 1
        }

        if (inQuotes) {
            throw IllegalArgumentException("CSV contém campo com aspas não fechadas")
        }

        if (currentField.isNotEmpty() || currentRecord.isNotEmpty()) {
            currentRecord.add(currentField.toString())
            records.add(currentRecord.toList())
        }

        return records
    }
}
