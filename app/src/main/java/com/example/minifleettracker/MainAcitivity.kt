package com.example.minifleettracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.minifleettracker.helper.ViewModelFactory
import com.example.minifleettracker.ui.LeafletMapScreen
import com.example.minifleettracker.ui.SensorDashboard
import com.example.minifleettracker.viewmodel.FleetViewModel

class MainActivity : ComponentActivity() {
    private val fleetViewModel: FleetViewModel by viewModels<FleetViewModel>{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FleetTrackerApp(fleetViewModel)
            LaunchedEffect(Unit) {
                fleetViewModel.events.collect { message ->
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        }

        fleetViewModel.startSimulation()
    }
}

@Composable
fun FleetTrackerApp(viewModel: FleetViewModel) {
    val vehicleData by viewModel.vehicleData.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        LeafletMapScreen(viewModel)
        SensorDashboard(vehicleData)
    }
}