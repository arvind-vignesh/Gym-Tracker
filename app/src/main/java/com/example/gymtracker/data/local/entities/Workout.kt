package com.example.gymtracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routineId: Int? = null,
    val name: String,
    val startTime: Long,
    val endTime: Long? = null
)
