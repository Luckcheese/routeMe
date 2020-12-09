package com.luckcheese.b2w.services

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.model.Place
import com.luckcheese.b2w.services.permission.LocationPermissionService

class MapService(
    private val locationProvider: FusedLocationProviderClient,
    private val locationPermissionService: LocationPermissionService
): OnSuccessListener<Location> {

    private val zoomLevel = 16F

    var map: GoogleMap? = null
        set(value) {
            field = value
            update()
        }
    var selectedPlace: Place? = null
        set(value) {
            field = value
            update()
        }
    var userLocation: Location? = null
        set(value) {
            field = value
            setUserLatLng(value)
        }

    private var userLatLng: LatLng? = null

    init {
        showUserLocation(false)
    }

    fun centerOnMyLocation() {
        showUserLocation(true)
    }

    @SuppressLint("MissingPermission")
    private fun showUserLocation(force: Boolean) {
        locationPermissionService.check(force) { isGranted ->
            if (isGranted) {
                locationProvider.lastLocation.addOnSuccessListener(this)
            }
        }
    }

    private fun update() {
        if (map == null) return

        selectedPlace?.latLng?.let {
            val marker = MarkerOptions()
                .position(it)
                .title(selectedPlace?.name ?: "")
            map?.addMarker(marker)
        }

        cameraPos()?.let { map?.moveCamera(it) }
    }

    private fun setUserLatLng(location: Location?) {
        location?.run {
            userLatLng = LatLng(latitude, longitude)
        }
    }

    private fun cameraPos(): CameraUpdate? {
        return selectedPlace?.latLng?.let { place ->
            return CameraUpdateFactory.newLatLngZoom(
                place,
                zoomLevel
            )
        }
    }

    // ----- OnSuccessListener -----

    override fun onSuccess(userLocation: Location) {
        this.userLocation = userLocation

        map?.clear()
        userLatLng?.let {
            val marker = MarkerOptions()
                .position(it)
            map?.addMarker(marker)
        }
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
            userLatLng,
            zoomLevel
        ))
    }
}
