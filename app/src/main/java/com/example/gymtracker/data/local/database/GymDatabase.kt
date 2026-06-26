package com.example.gymtracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymtracker.data.local.dao.RoutineDao
import com.example.gymtracker.data.local.dao.WorkoutDao
import com.example.gymtracker.data.local.entities.*

@Database(
    entities = [
        Routine::class,
        RoutineExercise::class,
        Workout::class,
        ExerciseLog::class,
        SetLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GymDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        const val DATABASE_NAME = "gym_tracker_db"
    }
}
