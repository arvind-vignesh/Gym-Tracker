package com.example.gymtracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.local.entities.Routine
import com.example.gymtracker.data.local.entities.RoutineExercise
import com.example.gymtracker.data.model.RoutineWithExercises
import com.example.gymtracker.data.repository.GymRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutinesViewModel(private val repository: GymRepository) : ViewModel() {

    val routines: StateFlow<List<RoutineWithExercises>> = repository.getAllRoutinesWithExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createRoutine(name: String, description: String, exercises: List<RoutineExercise>) {
        viewModelScope.launch {
            repository.insertRoutine(
                Routine(name = name, description = description),
                exercises
            )
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }
}
