package com.example.test_kotlin.parentalcontrol

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

/**
 * A disclosed, scheduled "wake-up" using WorkManager.
 *
 * This is the legitimate counterpart to the JobScheduler / AlarmManager
 * self-ping tricks stalkerware uses to stay alive. It runs on the OS's own
 * battery-friendly schedule (minimum 15 minutes), records a heartbeat the user
 * can see on the transparency screen, and makes sure the visible foreground
 * service is running -- again, only while consent is active. It never tries to
 * hide, and it stops scheduling itself the moment monitoring is turned off.
 */
class HeartbeatWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val consent = ConsentStore(applicationContext)
        if (!consent.monitoringEnabled) {
            cancel(applicationContext)
            return Result.success()
        }

        consent.lastHeartbeatMillis = System.currentTimeMillis()
        MonitoringService.start(applicationContext)
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "parental_control_heartbeat"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<HeartbeatWorker>(
                15, TimeUnit.MINUTES
            ).setConstraints(Constraints.Builder().build()).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
