package com.example.gymtracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.local.entities.ExerciseLog
import com.example.gymtracker.data.local.entities.SetLog
import com.example.gymtracker.data.local.entities.Workout
import com.example.gymtracker.data.model.RoutineWithExercises
import com.example.gymtracker.data.repository.GymRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val repository: GymRepository,
    private val routineId: Int? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    init {
        startWorkout()
    }

    private fun startWorkout() {
        viewModelScope.launch {
            val workoutId = repository.insertWorkout(
                Workout(
                    routineId = routineId,
                    name = "New Workout",
                    startTime = System.currentTimeMillis()
                )
            )
            _uiState.update { it.copy(workoutId = workoutId.toInt()) }

            if (routineId != null) {
                repository.getRoutineWithExercisesById(routineId).firstOrNull()?.let { routineWithExercises ->
                    _uiState.update { it.copy(workoutName = routineWithExercises.routine.name) }
                    routineWithExercises.exercises.forEach { routineExercise ->
                        addExercise(routineExercise.name, routineExercise.sets, routineExercise.targetReps, routineExercise.targetWeight)
                    }
                }
            }
        }
    }

    fun addExercise(name: String, sets: Int = 1, reps: Int = 0, weight: Double = 0.0) {
        val workoutId = _uiState.value.workoutId ?: return
        viewModelScope.launch {
            val exerciseLogId = repository.insertExerciseLog(
                ExerciseLog(workoutId = workoutId, name = name)
            )
            val newSets = (1..sets).map {
                SetLog(exerciseLogId = exerciseLogId.toInt(), reps = reps, weight = weight)
            }
            repository.insertSetLogs(newSets)
            refreshWorkoutData()
        }
    }

    fun updateSet(setLog: SetLog) {
        viewModelScope.launch {
            repository.updateSetLog(setLog)
            refreshWorkoutData()
        }
    }

    fun finishWorkout() {
        val workoutId = _uiState.value.workoutId ?: return
        viewModelScope.launch {
            val currentWorkout = repository.getWorkoutWithExercisesById(workoutId).firstOrNull()?.workout ?: return@launch
            repository.updateWorkout(currentWorkout.copy(endTime = System.currentTimeMillis()))
            _uiState.update { it.copy(isFinished = true) }
        }
    }

    private fun refreshWorkoutData() {
        val workoutId = _uiState.value.workoutId ?: return
        viewModelScope.launch {
            repository.getWorkoutWithExercisesById(workoutId).collect { workoutWithExercises ->
                _uiState.update { it.copy(workoutWithExercises = workoutWithExercises) }
            }
        }
    }
}

data class WorkoutUiState(
    val workoutId: Int? = null,
    val workoutName: String = "Workout",
    val workoutWithExercises: com.example.gymtracker.data.model.WorkoutWithExercises? = null,
    val isFinished: Boolean = false
)
