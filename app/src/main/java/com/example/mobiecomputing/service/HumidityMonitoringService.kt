package com.example.mobiecomputing.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.example.mobiecomputing.data.HumidityPreferences
import com.example.mobiecomputing.notifications.NotificationHelper
import com.example.mobiecomputing.sensors.AndroidHumiditySensorDataSource

class HumidityMonitoringService : Service() {
    private lateinit var sensorDataSource: AndroidHumiditySensorDataSource
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var humidityPreferences: HumidityPreferences

    private var latestHumidity: Float? = null
    private var wasAboveThreshold = false

    override fun onCreate() {
        super.onCreate()
        sensorDataSource = AndroidHumiditySensorDataSource(this)
        notificationHelper = NotificationHelper(this)
        humidityPreferences = HumidityPreferences(this)
        notificationHelper.ensureChannels()

        val notification = notificationHelper.buildMonitoringNotification(currentHumidity = null)
        ServiceCompat.startForeground(
            this,
            NotificationHelper.NOTIFICATION_MONITORING_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!sensorDataSource.isSensorAvailable()) {
            stopSelf()
            return START_NOT_STICKY
        }

        sensorDataSource.start { humidity ->
            latestHumidity = humidity
            updateForegroundNotification()
            evaluateAlert(humidity)
        }

        return START_STICKY
    }

    private fun updateForegroundNotification() {
        val notification = notificationHelper.buildMonitoringNotification(latestHumidity)
        ServiceCompat.startForeground(
            this,
            NotificationHelper.NOTIFICATION_MONITORING_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            }
        )
    }

    private fun evaluateAlert(humidity: Float) {
        val threshold = humidityPreferences.getThreshold()
        val isAboveThreshold = humidity > threshold

        if (isAboveThreshold && !wasAboveThreshold) {
            notificationHelper.showHumidityAlert(humidity, threshold)
        }

        wasAboveThreshold = isAboveThreshold
    }

    override fun onDestroy() {
        sensorDataSource.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
