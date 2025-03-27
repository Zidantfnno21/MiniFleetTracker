package com.example.minifleettracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.data.model.VehicleData
import com.example.minifleettracker.helper.UIEvent

@Composable
fun SensorDashboard(
    vehicleData: VehicleData?,
    isConnected: Boolean,
    alerts: UIEvent.ShowAlert?,
    onHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        )

        Text(
            text = "Vehicle Status",
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        if(isConnected){
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusCard(Icons.Default.Speed, "Speed", "${vehicleData?.speed ?: 0} km/h")
                StatusCard(
                    if (vehicleData?.engineOn == true) Icons.Default.Power else Icons.Default.PowerOff,
                    "Engine",
                    if (vehicleData?.engineOn == true) "ON" else "OFF"
                )
                StatusCard(
                    if (vehicleData?.doorOpen == true) Icons.Default.LockOpen else Icons.Default.Lock,
                    "Door",
                    if (vehicleData?.doorOpen == true) "OPEN" else "CLOSED"
                )
            }
        }else{
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Disconnected from MQTT",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                alerts?.let {
                    TextButton(onClick = it.onAction ?: {}) {
                        Text(it.actionLabel ?: "Retry")
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onHistoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(160.dp)
        ) {
            Icon(imageVector = Icons.Default.History, contentDescription = "Trip Log History")
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Trip Log")
        }
    }
}