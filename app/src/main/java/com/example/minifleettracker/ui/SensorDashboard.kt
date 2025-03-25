package com.example.minifleettracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.data.model.VehicleData
import kotlinx.coroutines.launch

@Composable
fun SensorDashboard(vehicleData: VehicleData?) {
    Column(Modifier.padding(16.dp)) {
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Text("Speed: ${vehicleData?.speed ?: 0} km/h")
        Text("Engine: ${if (vehicleData?.engineOn == true) "ON" else "OFF"}")
        Text("Door: ${if (vehicleData?.doorOpen == true) "OPEN" else "CLOSED"}")

        LaunchedEffect(vehicleData) {
            vehicleData?.let {
                if (it.speed > 80) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("⚡ Speeding Alert: ${it.speed} km/h")
                    }
                }

                if (it.doorOpen && it.speed > 0) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("🚨 Door Open While Moving!")
                    }
                }

                if (it.engineOn) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("🚗 Engine Started!")
                    }
                }

                if (!it.engineOn) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("🚗 Engine Stopped!")
                    }
                }

            }
        }

        SnackbarHost(hostState = snackbarHostState)

    }
}