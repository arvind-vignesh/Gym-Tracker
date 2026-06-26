package com.example.gymtracker.ui.routines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymtracker.data.model.RoutineWithExercises

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(
    routines: List<RoutineWithExercises>,
    onStartRoutine: (Int) -> Unit,
    onDeleteRoutine: (RoutineWithExercises) -> Unit,
    onCreateRoutine: () -> Unit
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Routines", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateRoutine,
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = { Text("New Routine") }
            )
        }
    ) { innerPadding ->
        if (routines.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No routines created yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(routines) { routineWithExercises ->
                    RoutineCard(
                        routineWithExercises = routineWithExercises,
                        onStart = { onStartRoutine(routineWithExercises.routine.id) },
                        onDelete = { onDeleteRoutine(routineWithExercises) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoutineCard(
    routineWithExercises: RoutineWithExercises,
    onStart: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    routineWithExercises.routine.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
            Text(
                routineWithExercises.routine.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "${routineWithExercises.exercises.size} Exercises",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Start Routine")
            }
        }
    }
}
