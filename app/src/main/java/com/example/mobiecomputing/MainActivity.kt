package com.example.mobiecomputing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobiecomputing.navigation.AppNavigation
import com.example.mobiecomputing.ui.theme.MobiecomputingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobiecomputingTheme {
                AppNavigation()
            }
        }
    }
}