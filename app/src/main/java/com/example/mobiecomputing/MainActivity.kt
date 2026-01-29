package com.example.mobiecomputing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobiecomputing.ui.theme.MobiecomputingTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme

import android.content.res.Configuration

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.mobiecomputing.data.SampleData
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize


//for hw2
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.automirrored.filled.ArrowBack




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobiecomputingTheme {
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
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row (modifier = Modifier.padding(all=8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "pfp",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

sealed class Screen(val route: String) {
    data object Main : Screen("messages")
    data object Second : Screen("settings")
}


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

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    MobiecomputingTheme {
        Surface {
            MessageCard(
                msg = Message("Jonppe", "Moso Moso")
            )
        }
    }
}

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