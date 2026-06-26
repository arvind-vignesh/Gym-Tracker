package com.example.gymtracker

import android.app.Application
import com.example.gymtracker.data.local.database.DatabaseModule

class GymTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Pre-initialize database
        DatabaseModule.provideDatabase(this)
    }
}
