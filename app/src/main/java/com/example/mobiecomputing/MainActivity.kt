package com.example.mobiecomputing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobiecomputing.navigation.AppNavigation
import com.example.mobiecomputing.ui.theme.MobiecomputingTheme
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.example.mobiecomputing.notifications.NotificationHelper

class MainActivity : ComponentActivity() {
    private var openMonitorNonce by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consumeMonitorIntent(intent)

        setContent {
            MobiecomputingTheme {
                AppNavigation(openMonitorNonce = openMonitorNonce)
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        consumeMonitorIntent(intent)
    }

    private fun consumeMonitorIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(NotificationHelper.EXTRA_OPEN_MONITOR, false) == true) {
            openMonitorNonce += 1
        }
    }
}