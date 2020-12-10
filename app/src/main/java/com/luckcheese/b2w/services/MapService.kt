package com.luckcheese.b2w.services

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.model.Place
import com.luckcheese.b2w.services.permission.LocationPermissionService

class MapService(
    private val locationProvider: FusedLocationProviderClient,
    private val locationPermissionService: LocationPermissionService,
    private val routeService: RouteService
): OnSuccessListener<Location>, GoogleMap.OnMarkerClickListener {

    private var showingInfoWindow = false
    private var showingRoute: Polyline? = null
    private val zoomLevel = 16F
    private val cameraPadding = 200

    var map: GoogleMap? = null
        set(value) {
            field = value
            showingInfoWindow = false
            value?.setOnMarkerClickListener(this)
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
    var showRouteBtn: View? = null
        set(value) {
            field = value
            value?.visibility = View.GONE
            update()
        }

    private var userLatLng: LatLng? = null
        set(value) {
            field = value
            update()
        }

    init {
        showUserLocation(false)

        routeService.errorCallback = { e: Throwable ->
            val errorMessage = "Error computing route between " +
                    "$userLatLng and $selectedPlace"
            Log.w("RouteService", errorMessage, e)
        }
    }

    fun centerOnMyLocation() {
        showUserLocation(true)
    }

    fun showRoute() {
        if (userLatLng == null || selectedPlace == null) return
        if (map == null) return

        routeService.getDirection(
            userLatLng!!,
            selectedPlace!!
        ) { showRoute(it) }
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

        showingInfoWindow = false

        showRouteBtn?.visibility = if (userLatLng != null && selectedPlace != null) View.VISIBLE
        else View.GONE

        showingRoute?.let { it.remove() }
        map?.clear()
        selectedPlace?.latLng?.let {
            val marker = MarkerOptions()
                .position(it)
                .title(selectedPlace?.name ?: "")
            selectedPlace?.address?.let { a -> marker.snippet(a) }
            map?.addMarker(marker)
        }

        userLatLng?.let {
            val marker = MarkerOptions()
                .position(it)
            map?.addMarker(marker)
        }

        cameraPos()?.let { map?.moveCamera(it) }
    }

    private fun showRoute(route: List<LatLng>) {
        showingRoute?.let { it.remove() }
        showingRoute = map?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(route)
        )
    }

    private fun setUserLatLng(location: Location?) {
        location?.run {
            userLatLng = LatLng(latitude, longitude)
        }
    }

    private fun cameraPos(): CameraUpdate? {
        return selectedPlace?.latLng?.let { place ->
            userLatLng?.let { user ->
                val bounds = LatLngBounds
                    .builder()
                    .include(place)
                    .include(user)
                    .build()

                return CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    cameraPadding
                )
            }

            return CameraUpdateFactory.newLatLngZoom(
                place,
                zoomLevel
            )
        } ?: userLatLng?.let { user ->
            return CameraUpdateFactory.newLatLngZoom(
                user,
                zoomLevel
            )
        }
    }

    // ----- OnSuccessListener -----

    override fun onSuccess(userLocation: Location) {
        this.userLocation = userLocation
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
            userLatLng,
            zoomLevel
        ))
    }

    // ----- GoogleMap.OnMarkerClickListener -----

    override fun onMarkerClick(marker: Marker): Boolean {
        // easy way to ignore user location marker as app will always only show at most two markers
        if (marker.title != null) {
            if (!showingInfoWindow) {
                marker.showInfoWindow()
            }
            showingInfoWindow != showingInfoWindow
            return true
        }

        return false
    }
}
