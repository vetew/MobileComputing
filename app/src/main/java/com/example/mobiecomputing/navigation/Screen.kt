package com.example.mobiecomputing.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("messages")
    data object Second : Screen("settings")
}