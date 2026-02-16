package com.example.mobiecomputing.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiecomputing.data.HumidityPreferences
import com.example.mobiecomputing.service.HumidityMonitoringService
import com.example.mobiecomputing.sensors.AndroidHumiditySensorDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat

class MonitorViewModel(application: Application) : AndroidViewModel(application) {
    private val sensorDataSource = AndroidHumiditySensorDataSource(application)
    private val humidityPreferences = HumidityPreferences(application)

    private val _uiState = MutableStateFlow(
        MonitorUiState(
            threshold = humidityPreferences.getThreshold(),
            sensorAvailable = sensorDataSource.isSensorAvailable()
        )
    )
    val uiState: StateFlow<MonitorUiState> = _uiState.asStateFlow()

    init {
        if (sensorDataSource.isSensorAvailable()) {
            sensorDataSource.start { humidity ->
                _uiState.update { it.copy(currentHumidity = humidity) }
            }
        }
    }

    fun updateThreshold(input: String) {
        val parsed = input.toFloatOrNull() ?: return
        val bounded = parsed.coerceIn(0f, 100f)
        _uiState.update { it.copy(threshold = bounded) }

        viewModelScope.launch {
            humidityPreferences.setThreshold(bounded)
        }
    }

    fun startMonitoringService() {
        val context = getApplication<Application>()
        val serviceIntent = Intent(context, HumidityMonitoringService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
        _uiState.update { it.copy(serviceRunning = true) }
    }

    fun stopMonitoringService() {
        val context = getApplication<Application>()
        val serviceIntent = Intent(context, HumidityMonitoringService::class.java)
        context.stopService(serviceIntent)
        _uiState.update { it.copy(serviceRunning = false) }
    }

    override fun onCleared() {
        sensorDataSource.stop()
        super.onCleared()
    }
}

data class MonitorUiState(
    val currentHumidity: Float? = null,
    val threshold: Float = HumidityPreferences.DEFAULT_THRESHOLD,
    val sensorAvailable: Boolean = false,
    val serviceRunning: Boolean = false
)
