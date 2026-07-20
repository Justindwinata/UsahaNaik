# Notification Architecture

## Purpose

UN-0012 turns UsahaNaik reminders from local in-app planning cards into permission-safe local notification work while keeping the app useful when notifications are denied.

The feature supports:

- Daily financial tracking reminders.
- Weekly plan task reminders.
- Scheduled content reminders.
- Weekly retrospective reminders.
- Business report review reminders.

## Android Platform Decisions

### Notification Permission

Android 13 and higher uses the `POST_NOTIFICATIONS` runtime permission for non-exempt notifications. UsahaNaik declares the permission in the manifest, but the app must request it only after a user action from the reminder UI.

Official reference: [Notification runtime permission](https://developer.android.com/develop/ui/compose/notifications/notification-permission)

Implementation decision:

- Do not request permission on launch.
- Show a permission explanation card in Profile.
- Request permission only when the user taps `Enable Notifications`.
- If permission is denied, keep reminders visible in Dashboard/Profile as in-app fallback.

### Notification Channel

Android 8.0 and higher requires notifications to use a channel. UsahaNaik uses one channel for business routine reminders.

Official reference: [Create and manage notification channels](https://developer.android.com/develop/ui/compose/notifications/channels)

Implementation decision:

- Create one `Business reminders` channel.
- Channel creation is idempotent and safe to call repeatedly.
- Reminder notifications use non-sensitive copy from `ReminderMessageFactory`.

### WorkManager Scheduling

UsahaNaik uses WorkManager for approximate local reminder execution. WorkManager supports unique work names and cancellation, which fits reminder updates and delete/pause actions.

Official references:

- [Managing work](https://developer.android.com/develop/background-work/background-tasks/persistent/how-to/manage-work)
- [Work states](https://developer.android.com/develop/background-work/background-tasks/persistent/how-to/states)
- [WorkManager API reference](https://developer.android.com/reference/kotlin/androidx/work/WorkManager)

Implementation decision:

- Schedule one unique work item per active reminder.
- Use stable unique work names such as `business_reminder_42`.
- Use replace behavior when reminder content or time changes.
- Cancel work when reminder is paused, disabled, completed, or deleted.
- Treat delivery as approximate because WorkManager is subject to OS scheduling and battery policies.

### Exact Alarms Avoided

Exact alarms are for precise, time-critical user experiences. UMKM routine reminders do not require exact-to-the-minute delivery, and exact alarms add special permission and battery policy complexity.

Official reference: [Schedule alarms](https://developer.android.com/develop/background-work/services/alarms)

Implementation decision:

- Do not request `SCHEDULE_EXACT_ALARM`.
- Do not use exact alarms in UN-0012.
- Use approximate WorkManager scheduling and clear limitation copy.

## App Behavior

When notifications are allowed:

- Active reminders can enqueue WorkManager notification work.
- Updating an active reminder replaces the existing scheduled work.
- Pausing or deleting a reminder cancels the unique work item.

When notifications are denied:

- Reminder save/update still succeeds.
- Scheduler returns a permission fallback state.
- Dashboard/Profile continue to show next active reminder and reminder counts.

When a worker runs:

- It loads the reminder from the local Room database.
- If the reminder is missing or not active, it exits successfully without showing a notification.
- If permission is denied, it exits successfully without crashing.
- If permission is granted, it posts a local notification that opens the app.

## QA Plan

Automated tests cover:

- Stable unique work names.
- Active reminder scheduling.
- Cancel behavior for paused/deleted reminders.
- Permission denied fallback.
- Worker input handling.
- Worker behavior for missing or disabled reminders.
- ViewModel schedule/cancel calls.

Manual QA requires an emulator or physical Android device:

- Android 13+ permission prompt from the reminder UI.
- Permission granted flow.
- Permission denied flow.
- In-app fallback after denial.
- Reminder scheduling after enable/update.
- Reminder cancellation after pause/delete.
- Notification tap opens the app.

If no emulator/device is attached, notification delivery must not be claimed as manually tested.
