package com.example.gymtracker.data.local.dao

import androidx.room.*
import com.example.gymtracker.data.local.entities.ExerciseLog
import com.example.gymtracker.data.local.entities.SetLog
import com.example.gymtracker.data.local.entities.Workout
import com.example.gymtracker.data.model.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY startTime DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY startTime DESC")
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :id")
    fun getWorkoutWithExercisesById(id: Int): Flow<WorkoutWithExercises?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLog(exerciseLog: ExerciseLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetLogs(setLogs: List<SetLog>)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Update
    suspend fun updateSetLog(setLog: SetLog)

    @Delete
    suspend fun deleteWorkout(workout: Workout)
}
