package com.luckcheese.b2w.services.permission

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class PermissionsService(
    private val activity: AppCompatActivity
) : ActivityResultCallback<Boolean> {

    protected abstract val permission: String
    protected abstract fun helpMessage(context: Context): String
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(helpMessage(activity))
            dialog.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { _, _ ->
                permissionHandler.launch(permission)
            })
            dialog.show()
        }
        else {
            permissionHandler.launch(permission)
        }
    }

    // ----- ActivityResultCallback -----

    override fun onActivityResult(isGranted: Boolean) {
        if (!isGranted) {

        }
        callback(isGranted)
    }
}
