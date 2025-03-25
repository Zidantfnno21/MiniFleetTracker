package com.example.minifleettracker.helper

import android.content.Context
import com.example.minifleettracker.data.database.TripLogDatabase
import com.example.minifleettracker.data.repository.MiniFleetTrackerRepository

object Injection {
    fun provideRepository(context: Context): MiniFleetTrackerRepository {
        val database = TripLogDatabase.getDatabase(context)
        val dao = database.tripLogDao()
        return MiniFleetTrackerRepository.getInstance(dao)
    }
}