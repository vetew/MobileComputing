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
import kotlinx.coroutines.flow.collectLatest
import coil.compose.AsyncImage
import com.example.mobiecomputing.data.ProfileRepository
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer


@Composable
fun MessagesScreen(onGoToSecond: () -> Unit, onGoToMonitor: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { ProfileRepository(context) }
    var profileName by remember { mutableStateOf("Oletus käyttäjä") }
    var profileImageUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(repository) {
        repository.profileFlow.collectLatest { profile ->
            profileName = profile.name
            profileImageUri = profile.imageUri
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Button(
            onClick = onGoToMonitor,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Text("Löyly")
        }
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

        Column(modifier = Modifier.padding(top = 48.dp)) {
            ProfileHeader(
                name = profileName,
                imageUri = profileImageUri,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Conversation(
                messages = SampleData.conversationSample,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ProfileHeader(name: String, imageUri: String?, modifier: Modifier = Modifier) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 16.dp, vertical = 12.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, modifier = Modifier.size(56.dp)) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Profiilikuva",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ei kuvaa", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Profiilikuva ja nimi näkyvät tässä.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun MessagesScreenPreview() {
    MessagesScreen(onGoToSecond = {}, onGoToMonitor = {})
}
