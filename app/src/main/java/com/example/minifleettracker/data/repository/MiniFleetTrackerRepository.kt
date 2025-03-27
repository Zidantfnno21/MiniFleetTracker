package com.example.minifleettracker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isConnected = MutableLiveData<Boolean>(true) // Track connection status
    val isConnected: LiveData<Boolean> = _isConnected

    private val mqttService = MqttHelper (
        onMessageReceived = {message ->
            val data = Gson().fromJson(message, VehicleData::class.java)
            runBlocking {
                updateSensorData(
                    data.latitude,
                    data.longitude,
                    data.speed,
                    data.engineOn,
                    data.doorOpen)
            }
        },
        onConnectionStatusChanged = { isConnected ->
            _isConnected.postValue(isConnected)
        }
    )

    fun startMqtt() {
        mqttService.connect()
    }

    fun stopMqtt() {
        mqttService.disconnect()
    }

    private suspend fun updateSensorData(
        latitude: Double,
        longitude: Double,
        speed: Int,
        engineOn: Boolean,
        doorOpen: Boolean
    ) {
        val data = VehicleData(
            latitude = latitude,
            longitude = longitude,
            speed = speed,
            engineOn = engineOn,
            doorOpen = doorOpen
        )

        _sensorData.emit(data)

         saveTripLog(
            latitude = data.latitude,
            longitude = data.longitude,
            speed = data.speed,
            engineOn = data.engineOn,
            doorOpen = data.doorOpen
        )
    }

     private suspend fun saveTripLog(
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