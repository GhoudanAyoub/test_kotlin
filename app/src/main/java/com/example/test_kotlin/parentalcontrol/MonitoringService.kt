package com.example.test_kotlin.parentalcontrol

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder

/**
 * A foreground monitoring service written the *honest* way.
 *
 * It demonstrates the same Android lifecycle APIs stalkerware abuses
 * (START_STICKY so the system restarts it, a foreground service so it keeps
 * running) but uses them transparently:
 *
 *  - It refuses to run unless the device owner enabled monitoring in the app.
 *  - It always posts a NON-dismissible, clearly-worded notification that names
 *    the tool and the supervisor, and offers a one-tap "Stop" action.
 *  - It collects nothing here beyond a screen-time summary the user can see in
 *    the app. There is no SMS, contacts, call-log, location, camera, or mic
 *    capture — those are the hallmarks of covert surveillance, not parental
 *    control.
 */
class MonitoringService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val consent = ConsentStore(this)

        // Hard stop if consent was withdrawn. A legitimate tool never outlives
        // the user's permission to run.
        if (!consent.monitoringEnabled || intent?.action == ACTION_STOP) {
            consent.monitoringEnabled = false
            stopForegroundCompat()
            stopSelf()
            return START_NOT_STICKY
        }

        startForeground(NOTIFICATION_ID, buildNotification(consent.supervisorLabel))

        // Ensure the disclosed, scheduled heartbeat is registered.
        HeartbeatWorker.schedule(this)

        // START_STICKY: if the OS kills us under memory pressure it recreates
        // the service. This is the legitimate use of the same flag stalkerware
        // relies on to be hard to remove -- the difference is the visible
        // notification and the user's ability to stop it at any time.
        return START_STICKY
    }

    private fun buildNotification(supervisor: String): Notification {
        createChannel()

        val openApp = PendingIntent.getActivity(
            this,
            0,
            Intent(this, ParentalControlActivity::class.java),
            pendingFlags()
        )

        val stop = PendingIntent.getService(
            this,
            1,
            Intent(this, MonitoringService::class.java).setAction(ACTION_STOP),
            pendingFlags()
        )

        val who = if (supervisor.isBlank()) "your family organizer" else supervisor
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }

        return builder
            .setContentTitle("Parental monitoring is ON")
            .setContentText("Screen-time sharing is active, set up by $who. Tap to review or turn off.")
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setOngoing(true) // persistent, but paired with a visible Stop action
            .setContentIntent(openApp)
            // int-icon addAction works from API 16+ (avoids the API 23 Icon builder)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop monitoring", stop)
            .build()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Parental monitoring status",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows an ongoing reminder while monitoring is active."
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun stopForegroundCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
    }

    private fun pendingFlags(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    companion object {
        const val ACTION_STOP = "com.example.test_kotlin.parentalcontrol.STOP"
        private const val CHANNEL_ID = "parental_monitoring"
        private const val NOTIFICATION_ID = 4201

        fun start(context: Context) {
            val intent = Intent(context, MonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.startService(
                Intent(context, MonitoringService::class.java).setAction(ACTION_STOP)
            )
        }
    }
}
