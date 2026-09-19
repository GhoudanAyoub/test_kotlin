package com.example.test_kotlin.parentalcontrol

import android.content.Context

/**
 * Tiny persistence wrapper for the *device owner's* explicit choices.
 *
 * A legitimate parental-control tool only runs after the person who owns the
 * device has knowingly turned it on. Nothing here is hidden: the flags below
 * gate whether the monitoring service and its scheduled wake-ups are allowed
 * to run, and the UI always reflects the current value. This is the deliberate
 * opposite of stalkerware, which tries to run regardless of the user's wishes.
 */
class ConsentStore(context: Context) {

    private val prefs =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** True only after the user has read the disclosure and tapped "Enable". */
    var monitoringEnabled: Boolean
        get() = prefs.getBoolean(KEY_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_ENABLED, value).apply()

    /** A human-readable label for who set this up (shown in the notification). */
    var supervisorLabel: String
        get() = prefs.getString(KEY_SUPERVISOR, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SUPERVISOR, value).apply()

    /** Timestamp of the last recorded heartbeat, for the transparency screen. */
    var lastHeartbeatMillis: Long
        get() = prefs.getLong(KEY_HEARTBEAT, 0L)
        set(value) = prefs.edit().putLong(KEY_HEARTBEAT, value).apply()

    companion object {
        private const val PREFS_NAME = "parental_control_consent"
        private const val KEY_ENABLED = "monitoring_enabled"
        private const val KEY_SUPERVISOR = "supervisor_label"
        private const val KEY_HEARTBEAT = "last_heartbeat"
    }
}
