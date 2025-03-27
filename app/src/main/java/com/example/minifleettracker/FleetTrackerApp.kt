package com.example.minifleettracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minifleettracker.helper.BottomSheetType
import com.example.minifleettracker.helper.UIEvent
import com.example.minifleettracker.ui.component.LeafletMap
import com.example.minifleettracker.ui.component.SensorDashboard
import com.example.minifleettracker.ui.component.Snackbar
import com.example.minifleettracker.ui.component.TripLog
import com.example.minifleettracker.viewmodel.FleetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetTrackerApp(viewModel: FleetViewModel) {
    val sheetState = rememberBottomSheetScaffoldState()
    var currentSheet by remember { mutableStateOf(BottomSheetType.Sensor) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val alert by viewModel.alertEvents.collectAsState()
    val snackBar by viewModel.snackBarEvents.collectAsState()
    val isConnected by viewModel.isConnected.observeAsState(true)

    HandleSnackbar(snackBar, snackbarHostState, scope)

    Box(modifier = Modifier.fillMaxSize()) {
        Snackbar(snackbarHostState)
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetContent = {
                when (currentSheet) {
                    BottomSheetType.Sensor -> SensorDashboard(
                        vehicleData = viewModel.vehicleData.observeAsState().value,
                        isConnected = isConnected,
                        alerts = alert,
                        onHistoryClick = {
                            scope.launch {
                                sheetState.bottomSheetState.partialExpand()
                                currentSheet = BottomSheetType.TripLog
                                sheetState.bottomSheetState.expand()
                            }
                        }
                    )

                    BottomSheetType.TripLog -> TripLog(
                        logs = viewModel.tripLogs.observeAsState(emptyList()).value,
                        onClose = {
                            scope.launch {
                                sheetState.bottomSheetState.partialExpand()
                                currentSheet = BottomSheetType.Sensor
                                sheetState.bottomSheetState.expand()
                            }
                        }
                    )
                }
            },
            sheetPeekHeight = 100.dp,
            sheetDragHandle = null,
        ) {
            LeafletMap(viewModel = viewModel)
        }
    }
}

@Composable
private fun HandleSnackbar(
    snackBar: UIEvent.ShowSnackbar?,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    LaunchedEffect(snackBar) {
        snackBar?.message?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }
}

