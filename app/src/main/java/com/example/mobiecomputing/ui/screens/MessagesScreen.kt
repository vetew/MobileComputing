package com.example.mobiecomputing.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobiecomputing.data.SampleData
import com.example.mobiecomputing.ui.composables.Conversation

@Composable
fun MessagesScreen(onGoToSecond: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onGoToSecond,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings"
            )
        }

        Conversation(
            messages = SampleData.conversationSample,
            modifier = Modifier.padding(top = 48.dp)
        )
    }
}

@Preview
@Composable
fun MessagesScreenPreview() {
    MessagesScreen(onGoToSecond = {})
}
