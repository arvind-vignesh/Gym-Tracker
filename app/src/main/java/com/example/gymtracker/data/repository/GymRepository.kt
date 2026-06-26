package com.example.gymtracker.data.repository

import com.example.gymtracker.data.local.entities.*
import com.example.gymtracker.data.model.RoutineWithExercises
import com.example.gymtracker.data.model.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

interface GymRepository {
    // Routine operations
    fun getAllRoutines(): Flow<List<Routine>>
    fun getAllRoutinesWithExercises(): Flow<List<RoutineWithExercises>>
    fun getRoutineWithExercisesById(id: Int): Flow<RoutineWithExercises?>
    suspend fun insertRoutine(routine: Routine, exercises: List<RoutineExercise>)
    suspend fun updateRoutine(routine: Routine, exercises: List<RoutineExercise>)
    suspend fun deleteRoutine(routine: Routine)

    // Workout operations
    fun getAllWorkouts(): Flow<List<Workout>>
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>
    fun getWorkoutWithExercisesById(id: Int): Flow<WorkoutWithExercises?>
    suspend fun insertWorkout(workout: Workout): Long
    suspend fun insertExerciseLog(exerciseLog: ExerciseLog): Long
    suspend fun insertSetLogs(setLogs: List<SetLog>)
    suspend fun updateWorkout(workout: Workout)
    suspend fun updateSetLog(setLog: SetLog)
    suspend fun deleteWorkout(workout: Workout)
}
