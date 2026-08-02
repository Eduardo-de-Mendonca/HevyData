package org.example.model

import java.time.LocalDateTime

data class WorkoutRow(
    val title: String,
    val startTime: LocalDateTime,
    val exerciseTitle: String,
    val weightKg: Double?,
    val reps: Int?
)
