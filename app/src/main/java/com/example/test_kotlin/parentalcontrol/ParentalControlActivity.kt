package com.example.test_kotlin.parentalcontrol

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test_kotlin.R
import java.util.concurrent.TimeUnit

/**
 * The transparency dashboard for the consent-based parental-control demo.
 *
 * Everything the tool does is stated on this screen. The device owner enables
 * or disables monitoring here, sees exactly what is (and is not) collected,
 * grants the special "Usage access" permission by hand, and views the same
 * screen-time summary that would be shared. Nothing runs behind their back.
 */
class ParentalControlActivity : AppCompatActivity() {

    private lateinit var consent: ConsentStore
    private lateinit var usage: UsageTracker

    private lateinit var statusText: TextView
    private lateinit var supervisorInput: EditText
    private lateinit var toggleButton: Button
    private lateinit var usageAccessButton: Button
    private lateinit var summaryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parental_control)
        title = "Parental Control (Demo)"

        consent = ConsentStore(this)
        usage = UsageTracker(this)

        statusText = findViewById(R.id.status_text)
        supervisorInput = findViewById(R.id.supervisor_input)
        toggleButton = findViewById(R.id.toggle_button)
        usageAccessButton = findViewById(R.id.usage_access_button)
        summaryText = findViewById(R.id.summary_text)

        supervisorInput.setText(consent.supervisorLabel)

        toggleButton.setOnClickListener { onToggleMonitoring() }
        usageAccessButton.setOnClickListener { openUsageAccessSettings() }
        findViewById<Button>(R.id.refresh_button).setOnClickListener { refreshSummary() }
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun onToggleMonitoring() {
        if (consent.monitoringEnabled) {
            // Turning off must be instant and complete.
            consent.monitoringEnabled = false
            MonitoringService.stop(this)
            HeartbeatWorker.cancel(this)
            Toast.makeText(this, "Monitoring stopped.", Toast.LENGTH_SHORT).show()
        } else {
            consent.supervisorLabel = supervisorInput.text.toString().trim()
            consent.monitoringEnabled = true
            MonitoringService.start(this)
            HeartbeatWorker.schedule(this)
            maybeRequestNotificationPermission()
            Toast.makeText(this, "Monitoring enabled. A status notice is now shown.", Toast.LENGTH_LONG).show()
        }
        render()
    }

    private fun openUsageAccessSettings() {
        // Sends the user to the system screen where they -- and only they --
        // can grant usage access. The app cannot grant this to itself.
        try {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } catch (e: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    private fun maybeRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
        }
    }

    private fun render() {
        val on = consent.monitoringEnabled
        statusText.text = if (on) "Status: MONITORING ON" else "Status: monitoring off"
        toggleButton.text = if (on) "Turn monitoring OFF" else "Enable monitoring"

        usageAccessButton.text =
            if (usage.hasUsageAccess()) "Usage access: granted ✓"
            else "Grant usage access (required for screen time)"

        refreshSummary()
    }

    private fun refreshSummary() {
        val sb = StringBuilder()

        if (consent.lastHeartbeatMillis > 0) {
            val ago = DateUtils.getRelativeTimeSpanString(consent.lastHeartbeatMillis)
            sb.append("Last background check-in: ").append(ago).append("\n\n")
        }

        if (!usage.hasUsageAccess()) {
            sb.append("Grant \"Usage access\" above to see screen-time here.")
            summaryText.text = sb.toString()
            return
        }

        val top = usage.topAppsToday()
        if (top.isEmpty()) {
            sb.append("No screen-time recorded in the last 24h yet.")
        } else {
            sb.append("Screen time in the last 24h (top apps):\n")
            top.forEach { app ->
                val mins = TimeUnit.MILLISECONDS.toMinutes(app.totalMillis)
                sb.append("\n• ").append(app.packageName).append("  —  ").append(mins).append(" min")
            }
        }
        summaryText.text = sb.toString()
    }
}
