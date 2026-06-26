package com.example.gymtracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.local.entities.ExerciseLog
import com.example.gymtracker.data.local.entities.Workout

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        entity = ExerciseLog::class,
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val exercises: List<ExerciseWithSets>
)
