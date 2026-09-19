package com.example.test_kotlin.parentalcontrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Restarts the monitoring service after a reboot -- but only if the device
 * owner had already, knowingly enabled monitoring.
 *
 * RECEIVE_BOOT_COMPLETED is the same permission stalkerware uses to claw its
 * way back after a restart. The legitimate difference is consent: we check the
 * user's own stored choice first, and if they turned monitoring off (or never
 * turned it on) we do nothing. There is no attempt to re-enable ourselves.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != "android.intent.action.QUICKBOOT_POWERON"
        ) {
            return
        }

        val consent = ConsentStore(context)
        if (consent.monitoringEnabled) {
            MonitoringService.start(context)
            HeartbeatWorker.schedule(context)
        }
    }
}
