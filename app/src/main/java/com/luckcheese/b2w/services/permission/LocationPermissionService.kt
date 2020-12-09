package com.luckcheese.b2w.services.permission

import android.Manifest
import androidx.appcompat.app.AppCompatActivity

class LocationPermissionService(activity: AppCompatActivity) : PermissionsService(activity) {

    override var permission = Manifest.permission.ACCESS_FINE_LOCATION
}
