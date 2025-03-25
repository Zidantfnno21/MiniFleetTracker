package com.example.minifleettracker.data.repository

import androidx.lifecycle.LiveData
import com.example.minifleettracker.data.database.TripLog
import com.example.minifleettracker.data.database.TripLogDao
import com.example.minifleettracker.data.model.VehicleData
import com.example.minifleettracker.helper.MqttHelper
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking

class MiniFleetTrackerRepository private constructor(
    private val tripLogDao: TripLogDao
) {
    private val _sensorData = MutableSharedFlow<VehicleData>()
    val sensorData = _sensorData.asSharedFlow()

    private val mqttService = MqttHelper { message ->
        val data = Gson().fromJson(message, VehicleData::class.java)
        runBlocking {
            updateSensorData(data.speed, data.engineOn, data.doorOpen)
        }
    }

    fun startMqtt() {
        mqttService.connect()
    }

    private suspend fun updateSensorData(speed: Int, engineOn: Boolean, doorOpen: Boolean) {
        val data = VehicleData(
            latitude = 51.505,
            longitude = -0.09,
            speed = speed,
            engineOn = engineOn,
            doorOpen = doorOpen
        )

        _sensorData.emit(data)

        val tripLog = TripLog(
            timestamp = System.currentTimeMillis(),
            latitude = data.latitude,
            longitude = data.longitude,
            speed = data.speed,
            engineOn = data.engineOn,
            doorOpen = data.doorOpen
        )
        tripLogDao.insertTripLog(tripLog)
    }

     suspend fun saveTripLog(
        latitude: Double,
        longitude: Double,
        speed: Int,
        engineOn: Boolean,
        doorOpen: Boolean
    ) {
        val tripLog = TripLog(
            timestamp = System.currentTimeMillis(),
            latitude = latitude,
            longitude = longitude,
            speed = speed,
            engineOn = engineOn,
            doorOpen = doorOpen
        )
        tripLogDao.insertTripLog(tripLog)
    }

    fun getTripLogs(): LiveData<List<TripLog>> = tripLogDao.getAllTripLogs()

    companion object {
        @Volatile
        private var instance: MiniFleetTrackerRepository? = null
        fun getInstance(
            tripLogDao: TripLogDao
        ): MiniFleetTrackerRepository =
            instance ?: synchronized(this) {
                instance ?: MiniFleetTrackerRepository(tripLogDao)
            }.also { instance = it }
    }
}