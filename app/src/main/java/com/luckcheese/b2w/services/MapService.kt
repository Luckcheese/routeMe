package com.luckcheese.b2w.services

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place

class MapService() {

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

    private fun cameraPos(): CameraUpdate? {
        return selectedPlace?.latLng?.let { place ->
            return CameraUpdateFactory.newLatLngZoom(
                place,
                zoomLevel
            )
        }
    }
}
