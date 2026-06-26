package com.example.gymtracker.ui.workout

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.theme.GymTrackerTheme

// Data models for the expressive UI
data class ExpressiveSet(
    val id: String,
    val weight: String,
    val reps: String,
    val isCompleted: Boolean = false
)

data class ExpressiveExercise(
    val id: String,
    val name: String,
    val sets: List<ExpressiveSet>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveWorkoutScreen(
    workoutName: String = "Morning Shred",
    onFinish: () -> Unit = {}
) {
    var exercises by remember {
        mutableStateOf(
            listOf(
                ExpressiveExercise(
                    "1", "Bench Press", listOf(
                        ExpressiveSet("1-1", "60", "10", true),
                        ExpressiveSet("1-2", "60", "10")
                    )
                ),
                ExpressiveExercise(
                    "2", "Squats", listOf(
                        ExpressiveSet("2-1", "80", "8")
                    )
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            workoutName.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            "LIVE SESSION",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("DONE", fontWeight = FontWeight.Black)
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    val newId = (exercises.size + 1).toString()
                    exercises = exercises + ExpressiveExercise(newId, "New Exercise", emptyList())
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Exercise", modifier = Modifier.size(36.dp))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            itemsIndexed(exercises, key = { _, ex -> ex.id }) { exIndex, exercise ->
                ExerciseSection(
                    exercise = exercise,
                    onUpdateExercise = { updatedEx ->
                        val newList = exercises.toMutableList()
                        newList[exIndex] = updatedEx
                        exercises = newList
                    },
                    onRemoveExercise = {
                        exercises = exercises.filter { it.id != exercise.id }
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseSection(
    exercise: ExpressiveExercise,
    onUpdateExercise: (ExpressiveExercise) -> Unit,
    onRemoveExercise: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                exercise.name.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onRemoveExercise) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Header for sets
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SET", modifier = Modifier.width(40.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            Text("WEIGHT", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            Text("REPS", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(8.dp))

        exercise.sets.forEachIndexed { index, set ->
            SetRow(
                setNumber = index + 1,
                set = set,
                onUpdateSet = { updatedSet ->
                    val newSets = exercise.sets.toMutableList()
                    newSets[index] = updatedSet
                    onUpdateExercise(exercise.copy(sets = newSets))
                },
                onRemoveSet = {
                    val newSets = exercise.sets.filterIndexed { i, _ -> i != index }
                    onUpdateExercise(exercise.copy(sets = newSets))
                }
            )
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = {
                val lastSet = exercise.sets.lastOrNull()
                val newSet = ExpressiveSet(
                    id = "${exercise.id}-${exercise.sets.size + 1}",
                    weight = lastSet?.weight ?: "0",
                    reps = lastSet?.reps ?: "0"
                )
                onUpdateExercise(exercise.copy(sets = exercise.sets + newSet))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("ADD SET", fontWeight = FontWeight.ExtraBold)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetRow(
    setNumber: Int,
    set: ExpressiveSet,
    onUpdateSet: (ExpressiveSet) -> Unit,
    onRemoveSet: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (set.isCompleted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        else Color.Transparent,
        label = "bg"
    )

    SwipeToDismissRow(onDelete = onRemoveSet) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(backgroundColor, MaterialTheme.shapes.medium)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(
                        if (set.isCompleted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    setNumber.toString(),
                    color = if (set.isCompleted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LoggingInput(
                value = set.weight,
                onValueChange = { onUpdateSet(set.copy(weight = it)) },
                modifier = Modifier.weight(1f),
                suffix = "kg"
            )

            LoggingInput(
                value = set.reps,
                onValueChange = { onUpdateSet(set.copy(reps = it)) },
                modifier = Modifier.weight(1f),
                suffix = "reps"
            )

            IconButton(
                onClick = { onUpdateSet(set.copy(isCompleted = !set.isCompleted)) },
                modifier = Modifier.size(48.dp)
            ) {
                AnimatedContent(
                    targetState = set.isCompleted,
                    transitionSpec = {
                        scaleIn(animationSpec = spring(0.6f, 500f)) togetherWith
                                scaleOut(animationSpec = spring(0.6f, 500f))
                    },
                    label = "check"
                ) { isDone ->
                    if (isDone) {
                        Icon(
                            Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Icon(
                            Icons.Rounded.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoggingInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    suffix: String = ""
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(IntrinsicSize.Min),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        if (value.isEmpty()) Text("0", color = MaterialTheme.colorScheme.outline)
                        innerTextField()
                    }
                }
            )
            Spacer(Modifier.width(2.dp))
            Text(
                suffix,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissRow(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                    else -> Color.Transparent
                }, label = "dismiss"
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color, MaterialTheme.shapes.medium)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        enableDismissFromStartToEnd = false,
        content = { content() }
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ExpressiveWorkoutPreview() {
    GymTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExpressiveWorkoutScreen()
        }
    }
}
