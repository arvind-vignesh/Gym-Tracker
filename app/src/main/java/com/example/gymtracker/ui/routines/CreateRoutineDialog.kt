package com.example.gymtracker.ui.routines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gymtracker.data.local.entities.RoutineExercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, List<RoutineExercise>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val exercises = remember { mutableStateListOf<RoutineExercise>() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Create Routine") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Rounded.Close, contentDescription = "Close")
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { if (name.isNotBlank()) onSave(name, description, exercises.toList()) },
                            enabled = name.isNotBlank()
                        ) {
                            Text("SAVE")
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Routine Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Text("Exercises", style = MaterialTheme.typography.titleMedium)
                }
                items(exercises) { exercise ->
                    Text("• ${exercise.name} (${exercise.sets} sets)")
                }
                item {
                    Button(
                        onClick = {
                            exercises.add(RoutineExercise(routineId = 0, name = "Exercise ${exercises.size + 1}", sets = 3, targetReps = 10, targetWeight = 0.0))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Exercise")
                    }
                }
            }
        }
    }
}
