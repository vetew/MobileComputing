package com.example.mobiecomputing.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobiecomputing.ui.screens.MessagesScreen
import com.example.mobiecomputing.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
                }
            )
        }

        composable(Screen.Second.route) {
            SettingsScreen(
                onBackToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}