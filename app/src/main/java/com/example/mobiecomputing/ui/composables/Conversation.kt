package com.example.mobiecomputing.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobiecomputing.data.Message
import com.example.mobiecomputing.data.SampleData
import com.example.mobiecomputing.ui.theme.MobiecomputingTheme

@Composable
fun Conversation(
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview(name = "Light Mode")
@Composable
fun PreviewConversation() {
    MobiecomputingTheme {
        Conversation(SampleData.conversationSample)
    }
}
