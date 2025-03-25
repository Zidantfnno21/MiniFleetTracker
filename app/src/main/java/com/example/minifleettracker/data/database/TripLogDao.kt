package com.example.minifleettracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TripLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTripLog(tripLog: TripLog)

    @Query("SELECT * FROM trip_logs ORDER BY timestamp DESC")
    fun getAllTripLogs(): LiveData<List<TripLog>>

    @Query("DELETE FROM trip_logs")
    suspend fun clearLogs()
}