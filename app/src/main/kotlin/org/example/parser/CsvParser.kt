package org.example.parser

import java.nio.file.Files
import java.nio.file.Path
import java.io.File

import com.jsoizo.kotlincsv.csvReader
import com.jsoizo.kotlincsv.CsvDialect

val reader = csvReader()

object CsvParser {
    fun parse(path: Path): List<List<String>> = parse(Files.readString(path))

    fun parse(content: String): List<List<String>> {
        val result = reader.readAll(content)
        return result
    }
}
