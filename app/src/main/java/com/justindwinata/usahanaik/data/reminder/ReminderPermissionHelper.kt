package com.justindwinata.usahanaik.data.reminder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.justindwinata.usahanaik.domain.model.ReminderPermissionState

interface ReminderPermissionHelper {
    fun currentState(): ReminderPermissionState
}

class AndroidReminderPermissionHelper(
    private val context: Context
) : ReminderPermissionHelper {
    override fun currentState(): ReminderPermissionState {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            ReminderPermissionState.NotRequired
        } else {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (granted) ReminderPermissionState.Granted else ReminderPermissionState.Denied
        }
    }
}
