package com.example.mobiecomputing.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.mobiecomputing.MainActivity
import com.example.mobiecomputing.R

class NotificationHelper(private val context: Context) {
    private val manager = NotificationManagerCompat.from(context)

    fun ensureChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val monitoringChannel = NotificationChannel(
            CHANNEL_MONITORING,
            "kosteuden mittaus",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "taustapalvelu löylyyn"
        }

        val alertChannel = NotificationChannel(
            CHANNEL_ALERT,
            "kosteus ilmoitukset",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Ilmoitukset löylyn tunnistukseen"
        }

        notificationManager.createNotificationChannels(listOf(monitoringChannel, alertChannel))
    }

    fun buildMonitoringNotification(currentHumidity: Float?): Notification {
        val content = currentHumidity?.let { "Kosteus nyt: ${"%.1f".format(it)}%" }
            ?: "debug: odotetaan dataa"

        return NotificationCompat.Builder(context, CHANNEL_MONITORING)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Löylyn tunnistus käynnissä")
            .setContentText(content)
            .setOngoing(true)
            .build()
    }

    fun showHumidityAlert(humidity: Float, threshold: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_OPEN_MONITOR, true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_ALERT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ALERT)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Löyly tunnistettu")
            .setContentText("Kosteus ${"%.1f".format(humidity)}% ylitti löylyn rajan ${"%.1f".format(threshold)}%")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(NOTIFICATION_ALERT_ID, notification)
    }

    companion object {
        const val CHANNEL_MONITORING = "humidity_monitoring"
        const val CHANNEL_ALERT = "humidity_alert"
        const val NOTIFICATION_MONITORING_ID = 2001
        const val NOTIFICATION_ALERT_ID = 2002
        const val EXTRA_OPEN_MONITOR = "extra_open_monitor"
        private const val REQUEST_ALERT = 3001
    }
}
