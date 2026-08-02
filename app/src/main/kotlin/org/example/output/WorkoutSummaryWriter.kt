package org.example.output

import org.example.aggregate.WorkoutSummary
import java.nio.file.Files
import java.nio.file.Path

object WorkoutSummaryWriter {
    fun write(summary: List<WorkoutSummary>, outputPath: Path) {
        val content = buildString {
            summary.forEach { workout ->
                appendLine("${workout.title}:")
                workout.exercises.forEach { exercise ->
                    appendLine("    ${exercise.translatedTitle}:")
                    val weightText = exercise.maxWeightKg?.let { "$it kg" } ?: "sem peso"
                    appendLine("        Peso máximo: $weightText")
                    val repsText = if (exercise.reps.isEmpty()) {
                        "sem repetições"
                    } else {
                        exercise.reps.joinToString(", ")
                    }
                    appendLine("        Repetições: $repsText")
                    appendLine()
                }
            }
        }

        Files.writeString(outputPath, content.trimEnd() + System.lineSeparator())
    }
}
