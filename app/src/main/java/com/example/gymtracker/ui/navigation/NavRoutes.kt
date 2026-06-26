package com.example.gymtracker.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {
    @Serializable
    data object Dashboard : NavRoute

    @Serializable
    data object Routines : NavRoute

    @Serializable
    data object History : NavRoute

    @Serializable
    data class WorkoutSession(val routineId: Int? = null) : NavRoute
}
