package org.example

import org.example.aggregate.WorkoutAggregator
import org.example.parser.CsvParser
import org.example.parser.WorkoutCsvParser
import org.example.translation.TranslationLoader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class WorkoutProcessingTest {
    @Test
    fun `parse workout rows and aggregate summaries`() {
        val content = """"title","start_time","end_time","description","exercise_title","superset_id","exercise_notes","set_index","set_type","weight_kg","reps","distance_km","duration_seconds","rpe"
"Treino A","27 de jul. de 2026, 19:00","27 de jul. de 2026, 20:03","","Squat (Barbell)","","",0,"normal",15,12,,,
"Treino A","27 de jul. de 2026, 19:00","27 de jul. de 2026, 20:03","","Squat (Barbell)","","",1,"normal",14,12,,,
"Treino B","31 de jul. de 2026, 18:50","31 de jul. de 2026, 19:38","","Standing Calf Raise (Dumbbell)","","",0,"normal",10,12,,,
"Treino B","31 de jul. de 2026, 18:50","31 de jul. de 2026, 19:38","","Standing Calf Raise (Dumbbell)","","",1,"normal",10,12,,,
"Treino A","13 de jul. de 2026, 21:13","13 de jul. de 2026, 21:56","","Squat (Barbell)","","",0,"normal",14,12,,,
"Treino A","13 de jul. de 2026, 21:13","13 de jul. de 2026, 21:56","","Squat (Barbell)","","",1,"normal",14,12,,,
"""

        val rows = WorkoutCsvParser.parse(createTempFile(content))
        val translations = mapOf("Treino A" to "Treino A")
        val summary = WorkoutAggregator.aggregate(rows, translations, Clock.fixed(Instant.parse("2026-08-01T00:00:00Z"), ZoneId.of("UTC")))

        assertEquals(2, summary.size)
        assertEquals("Treino A", summary[0].title)
        assertEquals("Treino B", summary[1].title)
    }

    @Test
    fun `translation loader reads csv values`() {
        val content = """exercise_title,translation
Bench Press (Dumbbell),Supino (Halter)
"""

        val loaded = TranslationLoader.load(createTempFile(content))
        assertEquals(1, loaded.size)
        assertEquals("Supino (Halter)", loaded["Bench Press (Dumbbell)"])
    }

    private fun createTempFile(content: String): Path {
        return Files.createTempFile("test-workout", ".csv").also { Files.writeString(it, content) }
    }
}
