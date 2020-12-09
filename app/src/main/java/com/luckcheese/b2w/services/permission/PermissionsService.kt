package com.luckcheese.b2w.services.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class PermissionsService(
    private val activity: AppCompatActivity
) : ActivityResultCallback<Boolean> {

    protected abstract val permission: String
    private lateinit var callback: (isGranted: Boolean) -> Unit

    @Suppress("LeakingThis")
    private val permissionHandler = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        this
    )

    fun check(requestIfNeeded: Boolean = false, callback: (isGranted: Boolean) -> Unit) {
        if (!isGranted()) {
            if (requestIfNeeded) {
                this.callback = callback
                requestPermission()
            }
            else {
                callback(false)
            }
        }
        else {
            callback(true)
        }
    }

    private fun isGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        permissionHandler.launch(permission)
    }

    // ----- ActivityResultCallback -----

    override fun onActivityResult(isGranted: Boolean) {
        if (!isGranted) {

        }
        callback(isGranted)
    }
}
