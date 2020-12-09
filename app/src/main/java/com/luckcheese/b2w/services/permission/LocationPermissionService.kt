package com.luckcheese.b2w.services.permission

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.luckcheese.b2w.R

class LocationPermissionService(activity: AppCompatActivity) : PermissionsService(activity) {

    override var permission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun helpMessage(context: Context): String {
        return context.getString(R.string.request_location_permission)
    }
}
