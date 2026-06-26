package com.example.gymtracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.local.entities.ExerciseLog
import com.example.gymtracker.data.local.entities.SetLog

data class ExerciseWithSets(
    @Embedded val exercise: ExerciseLog,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseLogId"
    )
    val sets: List<SetLog>
)
