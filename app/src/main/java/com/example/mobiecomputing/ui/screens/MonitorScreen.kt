package com.example.mobiecomputing.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobiecomputing.ui.viewmodel.MonitorViewModel

@Composable
fun MonitorScreen(
    monitorViewModel: MonitorViewModel = viewModel()
) {
    val uiState by monitorViewModel.uiState.collectAsState()
    var thresholdInput by remember(uiState.threshold) { mutableStateOf(uiState.threshold.toString()) }

    val context = LocalContext.current
    var notificationsGranted by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationsGranted = granted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationsGranted) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Löyly Tunnistaja 3000", style = MaterialTheme.typography.headlineSmall)

        if (!uiState.sensorAvailable) {
            Text("Debug: ei oo sensoria")
        } else {
            Text(
                uiState.currentHumidity?.let { "Kosteus nyt: ${"%.1f".format(it)}%" }
                    ?: "debug: ootetaan dataa"
            )
        }

        OutlinedTextField(
            value = thresholdInput,
            onValueChange = {
                thresholdInput = it
                monitorViewModel.updateThreshold(it)
            },
            label = { Text("Tunnistusraja(%)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.sensorAvailable
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Text(
                if (notificationsGranted) "Ilmoitukset sallittu"
                else "Ilmoitukset ei sallittu"
            )

            if (!notificationsGranted) {
                Button(onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
                    Text("Anna lupa ilmoituksille")
                }
            }
        }

        Button(
            onClick = { monitorViewModel.startMonitoringService() },
            enabled = uiState.sensorAvailable
        ) {
            Text("Aloita taustamittaus")
        }

        Button(
            onClick = { monitorViewModel.stopMonitoringService() },
            enabled = uiState.serviceRunning
        ) {
            Text("Lopeta taustamittaus")
        }
    }
}
