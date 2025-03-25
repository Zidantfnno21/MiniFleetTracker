package com.example.minifleettracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_logs")
data class TripLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val speed: Int,
    val engineOn: Boolean,
    val doorOpen: Boolean
)
