package com.example.minifleettracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.viewmodel.FleetViewModel
import java.util.Date

@Composable
fun TripLogScreen(viewModel: FleetViewModel) {
    val logs by viewModel.tripLogs.observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (logs.isEmpty()) {
            item {
                Text("No trip logs yet.", style = MaterialTheme.typography.bodyMedium)
            }
        }else{
            items(logs) {log->
                Text(
                    "📍 ${log.latitude}, ${log.longitude} | 🚗 Speed: ${log.speed} km/h | 🕒 ${Date(log.timestamp)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}