package com.example.mobiecomputing.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobiecomputing.ui.screens.MessagesScreen
import com.example.mobiecomputing.ui.screens.SettingsScreen
import com.example.mobiecomputing.ui.screens.MonitorScreen
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppNavigation(openMonitorNonce: Int = 0) {
    val navController = rememberNavController()

    LaunchedEffect(openMonitorNonce) {
        if (openMonitorNonce > 0) {
            navController.navigate(Screen.Monitor.route) {
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MessagesScreen(
                onGoToSecond = {
                    navController.navigate(Screen.Second.route) {
                        launchSingleTop = true
                    }
                },
                onGoToMonitor = {
                    navController.navigate(Screen.Monitor.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Second.route) {
            SettingsScreen(
                onBackToMain = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Monitor.route) {
            MonitorScreen()
        }
    }
}