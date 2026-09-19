# Parental Control (Demo) — a consent-based counter-example to stalkerware

This package is a small, self-contained Android module built for a school /
security-course exercise on how covert surveillance apps ("stalkerware" / RATs)
work — and, more usefully, how the *same platform APIs* are used the honest way
by a legitimate parental-control tool.

It deliberately implements only the disclosed, consent-gated versions of the
mechanics. It is **not** spyware and cannot be used as spyware: it does nothing
without an on-screen toggle, it is always visible while running, and it collects
only per-app screen time.

## The mechanics, and the ethical line

| Mechanic | Stalkerware abuse | This demo's legitimate use |
|---|---|---|
| Foreground service + `START_STICKY` | Hide the notification / fake it, so the app is hard to find and kill | `MonitoringService` posts an honest, ongoing notification naming the tool and supervisor, with a one-tap **Stop** action |
| `RECEIVE_BOOT_COMPLETED` | Silently relaunch after reboot regardless of the user | `BootReceiver` restarts **only if** the device owner previously enabled monitoring; otherwise it does nothing |
| Scheduled wake-ups (JobScheduler / AlarmManager self-ping) | Resurrect a killed process to keep spying | `HeartbeatWorker` uses WorkManager on the OS's battery-friendly 15-min schedule, records a check-in the user can see, and cancels itself when monitoring is off |
| Permission grabbing | Request SMS/contacts/call-log/location/camera/mic all at once and exfiltrate | Uses **only** `PACKAGE_USAGE_STATS` — a *special access* the user must grant by hand in Settings — for screen time |

## What it collects

- Per-app foreground screen time (last 24h), only while monitoring is ON.
- A periodic background check-in timestamp.

## What it never touches

SMS/messages, contacts, call logs, location, camera, microphone, keystrokes,
or screen contents. The `MonitoringService` and `HeartbeatWorker` have no code
paths that read any of these, and none of those permissions are requested.

## Principles that make it "parental control" and not "stalkerware"

1. **Consent first** — nothing runs until the device owner taps *Enable*.
2. **Always visible** — a persistent, truthful notification whenever active.
3. **Reversible instantly** — one tap stops the service and cancels all schedules.
4. **Minimal data** — screen time only, shown to the user themselves.
5. **No self-defense against removal** — no device-admin lock, no hidden icon,
   no re-enabling itself.

## Files

- `ConsentStore.kt` — stores the owner's explicit on/off choice.
- `MonitoringService.kt` — foreground service (honest notification + Stop).
- `BootReceiver.kt` — consent-checked restart after reboot.
- `HeartbeatWorker.kt` — disclosed WorkManager heartbeat.
- `UsageTracker.kt` — screen-time via UsageStatsManager (special access).
- `ParentalControlActivity.kt` + `res/layout/activity_parental_control.xml` —
  the transparency dashboard where all of this is enabled, disclosed, and shown.
