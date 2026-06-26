package com.example.gymtracker.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "set_logs",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseLog::class,
            parentColumns = ["id"],
            childColumns = ["exerciseLogId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseLogId")]
)
data class SetLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseLogId: Int,
    val weight: Double,
    val reps: Int,
    val isCompleted: Boolean = false
)
