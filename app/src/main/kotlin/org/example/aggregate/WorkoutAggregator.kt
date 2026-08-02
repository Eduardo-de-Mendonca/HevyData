package org.example.aggregate

import org.example.model.WorkoutRow
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class WorkoutExerciseSummary(
    val originalTitle: String,
    val translatedTitle: String,
    val maxWeightKg: Double?,
    val reps: List<Int>
)

data class WorkoutSummary(
    val title: String,
    val startTime: LocalDateTime,
    val exercises: List<WorkoutExerciseSummary>,
    val isRecent: Boolean
)

object WorkoutAggregator {
    fun aggregate(
        rows: List<WorkoutRow>,
        translations: Map<String, String>,
        clock: Clock
    ): List<WorkoutSummary> {
        val now = LocalDateTime.now(clock)
        val recentThreshold = now.minus(30, ChronoUnit.DAYS)

        return rows
            .groupBy { it.title }
            .mapNotNull { (title, group) ->
                val latestStartTime = group.maxOfOrNull { it.startTime } ?: return@mapNotNull null
                val latestRows = group.filter { it.startTime == latestStartTime }

                val exercises = latestRows
                    .groupBy { it.exerciseTitle }
                    .map { (exerciseTitle, sets) ->
                        val translatedTitle = translations[exerciseTitle] ?: exerciseTitle
                        val maxWeightKg = sets.mapNotNull { it.weightKg }.maxOrNull()
                        val reps = sets.mapNotNull { it.reps }.sortedDescending()
                        WorkoutExerciseSummary(
                            originalTitle = exerciseTitle,
                            translatedTitle = translatedTitle,
                            maxWeightKg = maxWeightKg,
                            reps = reps
                        )
                    }
                    .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.translatedTitle })

                WorkoutSummary(
                    title = title,
                    startTime = latestStartTime,
                    exercises = exercises,
                    isRecent = !latestStartTime.isBefore(recentThreshold)
                )
            }
            .sortedWith(
                compareByDescending<WorkoutSummary> { it.isRecent }
                    .thenBy { it.title.lowercase() }
            )
    }
}
