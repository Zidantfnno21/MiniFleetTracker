package com.example.minifleettracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minifleettracker.data.database.TripLog
import com.example.minifleettracker.data.model.VehicleData
import com.example.minifleettracker.data.repository.MiniFleetTrackerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FleetViewModel(private val repository: MiniFleetTrackerRepository): ViewModel() {
    private val _vehicleData = MutableLiveData<VehicleData>()
    val vehicleData: LiveData<VehicleData> = _vehicleData

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    val tripLogs: LiveData<List<TripLog>> = repository.getTripLogs()

    private val route = listOf(
        Pair(51.505, -0.09),
        Pair(51.506, -0.091),
        Pair(51.507, -0.092)
    )

    init {
        repository.startMqtt()
        viewModelScope.launch {
           repository.sensorData.collect {data->
               _vehicleData.postValue(data)
           }
        }
    }

    fun startSimulation() {
        if (route.isEmpty()) return

        viewModelScope.launch {
            route.forEachIndexed { index, (lat, lng) ->
                delay(3000)
                val speed = (20..100).random()
                _vehicleData.postValue(
                    VehicleData(
                        lat,
                        lng,
                        speed,
                        engineOn = (0..1).random() == 1,
                        doorOpen = (0..1).random() == 1
                    )
                )

                if (index == route.lastIndex) {
                    _events.emit("✅ Simulation Complete!")
                }
            }
        }
    }
}