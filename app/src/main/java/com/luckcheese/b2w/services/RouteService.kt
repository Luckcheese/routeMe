package com.luckcheese.b2w.services

import android.app.Activity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult

class RouteService(
    private val activity: Activity,
    private var geoApiContext: GeoApiContext
) : PendingResult.Callback<DirectionsResult> {

    var routeCallback: ((route: List<LatLng>) -> Unit)? = null
    var errorCallback: ((e: Throwable) -> Unit)? = null

    fun getDirection(
        userLocation: LatLng,
        destinationPlace: Place,
        callback: (route: List<LatLng>) -> Unit
    ) {
        routeCallback = callback
        val request = DirectionsApi.newRequest(geoApiContext)
            .origin(com.google.maps.model.LatLng(
                userLocation.latitude, userLocation.longitude
            ))
            .destinationPlaceId(destinationPlace.id)
        request.setCallback(this)
    }

    // ----- PendingResult.Callback<DirectionsResult> -----

    override fun onResult(result: DirectionsResult) {
        val route = result
            .routes.first()
            .overviewPolyline
            .decodePath()
            .map { ll -> LatLng(ll.lat, ll.lng) }
        routeCallback?.let {
            activity.runOnUiThread { it(route) }
        }
    }

    override fun onFailure(e: Throwable) {
        errorCallback?.let {
            activity.runOnUiThread { it(e) }
        }
    }
}
