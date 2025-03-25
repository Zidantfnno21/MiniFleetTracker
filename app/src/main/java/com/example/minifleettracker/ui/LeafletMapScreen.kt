package com.example.minifleettracker.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.minifleettracker.viewmodel.FleetViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeafletMapScreen(viewModel: FleetViewModel){
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    AndroidView(
        factory = { webView.apply { settings.javaScriptEnabled = true } },
        update = { it.loadUrl("file:///android_asset/map.html") },
        modifier = Modifier.fillMaxSize()
    )

    val vehicleData by viewModel.vehicleData.observeAsState()

    vehicleData?.let {
        webView.evaluateJavascript(
            "window.updateVehiclePosition(${it.latitude}, ${it.longitude})",
            null
        )
    }
}