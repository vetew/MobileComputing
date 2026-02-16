package com.example.mobiecomputing.sensors

interface HumiditySensorDataSource {
    fun isSensorAvailable(): Boolean
    fun start(onHumidityChanged: (Float) -> Unit)
    fun stop()
}
