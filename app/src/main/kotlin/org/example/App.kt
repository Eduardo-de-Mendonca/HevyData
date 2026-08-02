package org.example

import org.example.aggregate.WorkoutAggregator
import org.example.output.WorkoutSummaryWriter
import org.example.parser.WorkoutCsvParser
import org.example.translation.TranslationLoader
import java.nio.file.Path
import java.time.Clock

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Uso: java -jar app.jar <caminho-do-input> [caminho-das-traducoes]")
        return
    }

    val inputPath = Path.of(args[0])
    if (!inputPath.toFile().exists()) {
        println("Arquivo de entrada não encontrado: $inputPath")
        return
    }

    val translationPath = when {
        args.size >= 2 -> Path.of(args[1])
        else -> inputPath.parent?.resolve("translations.csv") ?: Path.of("translations.csv")
    }

    val rows = WorkoutCsvParser.parse(inputPath)
    val translations = if (args.size >= 2 || translationPath.toFile().exists()) {
        TranslationLoader.load(translationPath)
    } else {
        emptyMap()
    }

    val summary = WorkoutAggregator.aggregate(rows, translations, Clock.systemDefaultZone())
    val outputPath = inputPath.parent.resolve(inputPath.fileName.toString().replaceAfterLast('.', "").removeSuffix(".") + "_output.txt")

    WorkoutSummaryWriter.write(summary, outputPath)
    println("Arquivo escrito em: $outputPath")
}
