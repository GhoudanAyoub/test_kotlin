package com.example.test_kotlin.parentalcontrol

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Process
import java.util.concurrent.TimeUnit

/**
 * Reads per-app screen time via UsageStatsManager.
 *
 * This is the one and only kind of data this demo collects, and it is the
 * classic legitimate parental-control signal. Crucially, the underlying
 * "Usage access" permission is a *special access* the user must grant by hand
 * in system Settings -- it cannot be silently obtained -- which is exactly why
 * it is appropriate for a transparent tool.
 */
class UsageTracker(private val context: Context) {

    data class AppUsage(val packageName: String, val totalMillis: Long)

    /** Whether the user has granted "Usage access" in Settings. */
    fun hasUsageAccess(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /** Top apps by foreground time over the last [hours] hours. */
    fun topAppsToday(hours: Long = 24, limit: Int = 8): List<AppUsage> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) return emptyList()
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return emptyList()

        val end = System.currentTimeMillis()
        val start = end - TimeUnit.HOURS.toMillis(hours)

        val stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
            ?: return emptyList()

        return stats
            .filter { it.totalTimeInForeground > 0 }
            .groupBy { it.packageName }
            .map { (pkg, list) -> AppUsage(pkg, list.sumOf { it.totalTimeInForeground }) }
            .sortedByDescending { it.totalMillis }
            .take(limit)
    }
}
