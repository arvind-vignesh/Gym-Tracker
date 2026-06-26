package com.example.gymtracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.local.entities.Routine
import com.example.gymtracker.data.local.entities.RoutineExercise

data class RoutineWithExercises(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val exercises: List<RoutineExercise>
)
