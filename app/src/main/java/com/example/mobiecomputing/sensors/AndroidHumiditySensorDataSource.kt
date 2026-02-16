package com.example.mobiecomputing.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AndroidHumiditySensorDataSource(context: Context) : HumiditySensorDataSource {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val humiditySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

    private var onHumidityChanged: ((Float) -> Unit)? = null

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val humidity = event?.values?.firstOrNull() ?: return
            onHumidityChanged?.invoke(humidity)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }

    override fun isSensorAvailable(): Boolean = humiditySensor != null

    override fun start(onHumidityChanged: (Float) -> Unit) {
        val sensor = humiditySensor ?: return
        this.onHumidityChanged = onHumidityChanged
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stop() {
        sensorManager.unregisterListener(listener)
        onHumidityChanged = null
    }
}
