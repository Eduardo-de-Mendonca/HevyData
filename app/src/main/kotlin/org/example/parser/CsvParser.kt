package org.example.parser

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Path

object CsvParser {
    private val format = CSVFormat.DEFAULT.builder()
        .setIgnoreSurroundingSpaces(true)
        .setTrim(true)
        .build()

    fun parse(path: Path): List<List<String>> = parse(Files.readString(path))

    fun parse(content: String): List<List<String>> {
        val normalized = content.removePrefix("\uFEFF")

        val parser = CSVParser(StringReader(normalized), format)
        return try {
            parser.records.map { record -> record.toList() }
        } finally {
            parser.close()
        }
    }
}
