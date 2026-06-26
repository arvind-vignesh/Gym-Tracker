package com.example.gymtracker.data.repository

import com.example.gymtracker.data.local.dao.RoutineDao
import com.example.gymtracker.data.local.dao.WorkoutDao
import com.example.gymtracker.data.local.entities.*
import com.example.gymtracker.data.model.RoutineWithExercises
import com.example.gymtracker.data.model.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

class GymRepositoryImpl(
    private val routineDao: RoutineDao,
    private val workoutDao: WorkoutDao
) : GymRepository {

    override fun getAllRoutines(): Flow<List<Routine>> = routineDao.getAllRoutines()

    override fun getAllRoutinesWithExercises(): Flow<List<RoutineWithExercises>> =
        routineDao.getAllRoutinesWithExercises()

    override fun getRoutineWithExercisesById(id: Int): Flow<RoutineWithExercises?> =
        routineDao.getRoutineWithExercisesById(id)

    override suspend fun insertRoutine(routine: Routine, exercises: List<RoutineExercise>) {
        val routineId = routineDao.insertRoutine(routine)
        val exercisesWithId = exercises.map { it.copy(routineId = routineId.toInt()) }
        routineDao.insertRoutineExercises(exercisesWithId)
    }

    override suspend fun updateRoutine(routine: Routine, exercises: List<RoutineExercise>) {
        routineDao.updateRoutine(routine)
        routineDao.deleteExercisesByRoutineId(routine.id)
        routineDao.insertRoutineExercises(exercises.map { it.copy(routineId = routine.id) })
    }

    override suspend fun deleteRoutine(routine: Routine) = routineDao.deleteRoutine(routine)

    override fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()

    override fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>> =
        workoutDao.getAllWorkoutsWithExercises()

    override fun getWorkoutWithExercisesById(id: Int): Flow<WorkoutWithExercises?> =
        workoutDao.getWorkoutWithExercisesById(id)

    override suspend fun insertWorkout(workout: Workout): Long = workoutDao.insertWorkout(workout)

    override suspend fun insertExerciseLog(exerciseLog: ExerciseLog): Long =
        workoutDao.insertExerciseLog(exerciseLog)

    override suspend fun insertSetLogs(setLogs: List<SetLog>) = workoutDao.insertSetLogs(setLogs)

    override suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)

    override suspend fun updateSetLog(setLog: SetLog) = workoutDao.updateSetLog(setLog)

    override suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)
}
