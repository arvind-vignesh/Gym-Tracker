package com.example.gymtracker.data.local.database

import android.content.Context
import androidx.room.Room
import com.example.gymtracker.data.repository.GymRepository
import com.example.gymtracker.data.repository.GymRepositoryImpl

object DatabaseModule {
    private var database: GymDatabase? = null
    private var repository: GymRepository? = null

    fun provideDatabase(context: Context): GymDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                GymDatabase::class.java,
                GymDatabase.DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
            database = instance
            instance
        }
    }

    fun provideRepository(context: Context): GymRepository {
        return repository ?: synchronized(this) {
            val db = provideDatabase(context)
            val instance = GymRepositoryImpl(db.routineDao(), db.workoutDao())
            repository = instance
            instance
        }
    }
}
