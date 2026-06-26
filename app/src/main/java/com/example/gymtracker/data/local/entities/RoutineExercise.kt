package com.example.gymtracker.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_exercises",
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class RoutineExercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routineId: Int,
    val name: String,
    val sets: Int,
    val targetReps: Int,
    val targetWeight: Double
)
