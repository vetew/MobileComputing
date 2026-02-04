package com.example.mobiecomputing.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onBackToMain: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onBackToMain,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.titleLarge)
            Text("Hirviän monta asetusta täälä niin")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onBackToMain = {})
}
