package com.example.minifleettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.minifleettracker.helper.ViewModelFactory
import com.example.minifleettracker.viewmodel.FleetViewModel
import com.example.minifleettracker.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val fleetViewModel: FleetViewModel by viewModels<FleetViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    FleetTrackerApp(fleetViewModel)
                }
            }
        }
    }
}