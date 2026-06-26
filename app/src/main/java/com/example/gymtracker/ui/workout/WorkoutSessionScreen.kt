package com.example.gymtracker.ui.workout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymtracker.data.local.entities.SetLog
import com.example.gymtracker.data.model.WorkoutWithExercises

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    workoutWithExercises: WorkoutWithExercises?,
    workoutName: String,
    onUpdateSet: (SetLog) -> Unit,
    onFinish: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(workoutName, fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onFinish) {
                        Text("FINISH", fontWeight = FontWeight.ExtraBold)
                    }
                }
            )
        }
    ) { innerPadding ->
        if (workoutWithExercises == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(workoutWithExercises.exercises) { exerciseWithSets ->
                    ExerciseLoggingCard(
                        name = exerciseWithSets.exercise.name,
                        sets = exerciseWithSets.sets,
                        onUpdateSet = onUpdateSet
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseLoggingCard(
    name: String,
    sets: List<SetLog>,
    onUpdateSet: (SetLog) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            
            sets.forEachIndexed { index, setLog ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Set ${index + 1}", modifier = Modifier.width(60.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${setLog.weight} kg x ${setLog.reps}", style = MaterialTheme.typography.bodyLarge)
                    }

                    IconButton(
                        onClick = { onUpdateSet(setLog.copy(isCompleted = !setLog.isCompleted)) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (setLog.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (setLog.isCompleted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = "Complete")
                    }
                }
            }
        }
    }
}
