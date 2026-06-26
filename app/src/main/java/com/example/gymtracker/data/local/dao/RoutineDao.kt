package com.example.gymtracker.data.local.dao

import androidx.room.*
import com.example.gymtracker.data.local.entities.Routine
import com.example.gymtracker.data.local.entities.RoutineExercise
import com.example.gymtracker.data.model.RoutineWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Transaction
    @Query("SELECT * FROM routines")
    fun getAllRoutinesWithExercises(): Flow<List<RoutineWithExercises>>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :id")
    fun getRoutineWithExercisesById(id: Int): Flow<RoutineWithExercises?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExercises(exercises: List<RoutineExercise>)

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("DELETE FROM routine_exercises WHERE routineId = :routineId")
    suspend fun deleteExercisesByRoutineId(routineId: Int)
}
