package com.example.gymtracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.gymtracker.data.local.database.DatabaseModule
import com.example.gymtracker.ui.dashboard.DashboardScreen
import com.example.gymtracker.ui.history.WorkoutHistoryScreen
import com.example.gymtracker.ui.routines.CreateRoutineDialog
import com.example.gymtracker.ui.routines.RoutinesScreen
import com.example.gymtracker.ui.viewmodel.HistoryViewModel
import com.example.gymtracker.ui.viewmodel.RoutinesViewModel
import com.example.gymtracker.ui.viewmodel.WorkoutViewModel
import com.example.gymtracker.ui.workout.ExpressiveWorkoutScreen
import com.example.gymtracker.ui.workout.WorkoutSessionScreen

@Composable
fun GymNavHost() {
    val backstack = rememberNavBackStack(NavRoute.Dashboard)
    val context = LocalContext.current
    val repository = remember { DatabaseModule.provideRepository(context) }

    Scaffold(
        bottomBar = {
            val currentRoute = backstack.last()
            if (currentRoute !is NavRoute.WorkoutSession) {
                GymBottomNavigation(
                    currentRoute = currentRoute as NavRoute,
                    onNavigate = { route ->
                        if (backstack.last() != route) {
                            while (backstack.size > 1) {
                                backstack.removeAt(backstack.size - 1)
                            }
                            backstack.removeAt(0)
                            backstack.add(route)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backstack,
            modifier = Modifier.padding(innerPadding),
            onBack = { if (backstack.size > 1) backstack.removeAt(backstack.size - 1) },
            entryProvider = entryProvider {
                entry<NavRoute.Dashboard> {
                    val viewModel: HistoryViewModel = viewModel { HistoryViewModel(repository) }
                    val history by viewModel.workoutHistory.collectAsState()

                    DashboardScreen(
                        onStartWorkout = { backstack.add(NavRoute.WorkoutSession()) },
                        onViewHistory = { 
                            while (backstack.size > 0) backstack.removeAt(backstack.size - 1)
                            backstack.add(NavRoute.History) 
                        },
                        recentWorkouts = history
                    )
                }
                entry<NavRoute.Routines> {
                    val viewModel: RoutinesViewModel = viewModel { RoutinesViewModel(repository) }
                    val routines by viewModel.routines.collectAsState()
                    var showCreateDialog by remember { mutableStateOf(false) }

                    RoutinesScreen(
                        routines = routines,
                        onStartRoutine = { id -> backstack.add(NavRoute.WorkoutSession(id)) },
                        onDeleteRoutine = { viewModel.deleteRoutine(it.routine) },
                        onCreateRoutine = { showCreateDialog = true }
                    )

                    if (showCreateDialog) {
                        CreateRoutineDialog(
                            onDismiss = { showCreateDialog = false },
                            onSave = { name, desc, exercises ->
                                viewModel.createRoutine(name, desc, exercises)
                                showCreateDialog = false
                            }
                        )
                    }
                }
                entry<NavRoute.History> {
                    val viewModel: HistoryViewModel = viewModel { HistoryViewModel(repository) }
                    val history by viewModel.workoutHistory.collectAsState()
                    
                    WorkoutHistoryScreen(history = history)
                }
                entry<NavRoute.WorkoutSession> { route ->
                    ExpressiveWorkoutScreen(
                        workoutName = "Active Session",
                        onFinish = { backstack.removeAt(backstack.size - 1) }
                    )
                }
            }
        )
    }
}

@Composable
fun GymBottomNavigation(
    currentRoute: NavRoute,
    onNavigate: (NavRoute) -> Unit
) {
    NavigationBar {
        val items = listOf(
            NavigationItem("Dashboard", Icons.Rounded.Dashboard, NavRoute.Dashboard),
            NavigationItem("Routines", Icons.Rounded.List, NavRoute.Routines),
            NavigationItem("History", Icons.Rounded.History, NavRoute.History)
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

private data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: NavRoute
)
