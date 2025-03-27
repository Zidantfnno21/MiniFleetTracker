package com.example.minifleettracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.minifleettracker.data.database.TripLog
import com.example.minifleettracker.data.model.VehicleData
import com.example.minifleettracker.data.repository.MiniFleetTrackerRepository
import com.example.minifleettracker.helper.UIEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FleetViewModel(private val repository: MiniFleetTrackerRepository): ViewModel() {
    private val _vehicleData = MutableLiveData<VehicleData>().apply {
        value = VehicleData(-6.1754, 106.8272, 0, engineOn = false, doorOpen = false)
    }
    val vehicleData: LiveData<VehicleData> = _vehicleData
    val isConnected: LiveData<Boolean> = repository.isConnected

    private val _snackBarEvents = MutableStateFlow(UIEvent.ShowSnackbar())
    val snackBarEvents = _snackBarEvents.asStateFlow()

    private val _alertEvents = MutableStateFlow(UIEvent.ShowAlert())
    val alertEvents = _alertEvents.asStateFlow()

    val tripLogs: LiveData<List<TripLog>> = repository.getTripLogs()

    fun retryConnection() {
        viewModelScope.launch {
            repository.stopMqtt()
            delay(500)
            repository.startMqtt()
        }
    }

    init {
        repository.startMqtt()

        viewModelScope.launch {
            repository.sensorData.collect { data ->
                _vehicleData.postValue(data)

                when {
                    data.speed > 80 -> {
                        _snackBarEvents.update{
                            it.copy(
                                message = "⚠️ Overspeeding! (${data.speed} km/h)"
                            )
                        }
                    }

                    data.doorOpen && data.speed > 0 -> {
                        _snackBarEvents.update{
                            it.copy(
                                message =  "🚪 Door is OPEN while moving!"
                            )
                        }
                    }

                    data.engineOn -> {
                        _snackBarEvents.update{
                            it.copy(
                                message = "🟢 Engine turned ON"
                            )
                        }
                    }

                    !data.engineOn -> {
                        _snackBarEvents.update{
                            it.copy(
                                message = "🔴 Engine turned OFF"
                            )
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            repository.isConnected.asFlow().collect { connected ->
                if (!connected) {
                    _alertEvents.update {
                        UIEvent.ShowAlert(
                            message = "Disconnected From MQTT",
                            actionLabel = "Retry",
                            onAction = ::retryConnection
                        )
                    }
                }
            }
        }
    }
}