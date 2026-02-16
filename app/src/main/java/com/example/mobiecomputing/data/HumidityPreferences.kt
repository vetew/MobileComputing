package com.example.mobiecomputing.data

import android.content.Context
import androidx.core.content.edit

class HumidityPreferences(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getThreshold(): Float = prefs.getFloat(KEY_THRESHOLD, DEFAULT_THRESHOLD)

    fun setThreshold(value: Float) {
        prefs.edit {
            putFloat(KEY_THRESHOLD, value)
        }
    }

    companion object {
        private const val PREF_NAME = "humidity_preferences"
        private const val KEY_THRESHOLD = "threshold"

        const val DEFAULT_THRESHOLD = 90f
    }
}
