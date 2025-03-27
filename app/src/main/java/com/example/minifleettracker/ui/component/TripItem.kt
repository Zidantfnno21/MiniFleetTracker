package com.example.minifleettracker.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.data.database.TripLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TripItem(log: TripLog, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "📍 ${log.latitude}, ${log.longitude}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                "🚗 Speed: ${log.speed} km/h",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                "🕒 ${SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault()).format(Date(log.timestamp))}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

