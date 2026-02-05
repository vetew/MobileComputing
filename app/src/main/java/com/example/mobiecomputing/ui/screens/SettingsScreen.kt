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
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.mobiecomputing.data.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.compose.rememberLauncherForActivityResult
import android.net.Uri
import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row

@Composable
fun SettingsScreen(onBackToMain: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { ProfileRepository(context) }
    val scope = rememberCoroutineScope()
    var profileName by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(repository) {
        repository.profileFlow.collectLatest { profile ->
            profileName = profile.name
            profileImageUri = profile.imageUri
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val storedUri = copyImageToInternalStorage(context, uri)
                repository.updateImageUri(storedUri)
            }
        }
    }

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
            Text("Muokkaa profiilin tietoja täällä.")

            OutlinedTextField(
                value = profileName,
                onValueChange = { newValue ->
                    profileName = newValue
                    scope.launch {
                        repository.updateName(newValue)
                    }
                },
                label = { Text("Nimi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUri != null) {
                        AsyncImage(
                            model = profileImageUri,
                            contentDescription = "Profiilikuva",
                            modifier = Modifier.size(160.dp)
                        )
                    } else {
                        Text("Ei profiilikuvaa valittuna")
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text("Valitse kuva")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Valitse kuva galleriasta.")
            }
        }
    }
}

private suspend fun copyImageToInternalStorage(context: Context, sourceUri: Uri): Uri {
    return withContext(Dispatchers.IO) {
        val fileName = "profile_${System.currentTimeMillis()}.jpg"
        val destination = File(context.filesDir, fileName)
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destination.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        destination.toUri()
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onBackToMain = {})
}
