package com.example.minifleettracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TripLog::class], version = 1, exportSchema = false)
abstract class TripLogDatabase : RoomDatabase() {
    abstract fun tripLogDao(): TripLogDao

    companion object {
        @Volatile
        private var INSTANCE: TripLogDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): TripLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripLogDatabase::class.java,
                    "trip_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}