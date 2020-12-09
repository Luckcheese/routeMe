package com.luckcheese.b2w

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, PlaceSelectionListener {

    private lateinit var mMap: GoogleMap

    private var selectedPlace: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        setupMaps()
        setupSearch()
    }

    private fun setupMaps() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupSearch() {
        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(this)
    }

    private fun showSelectedPlace(place: Place) {
        selectedPlace = place

        place.latLng?.let {
            val marker = MarkerOptions()
                .position(it)
                .title(selectedPlace?.name)
            mMap.addMarker(marker)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 16F))
        }
    }

    // ----- OnMapReadyCallback -----

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        selectedPlace?.let { showSelectedPlace(it) }
    }

    // ----- PlaceSelectionListener -----

    override fun onPlaceSelected(place: Place) {
        showSelectedPlace(place)
    }

    override fun onError(status: Status) {
        if (status != Status.RESULT_CANCELED) {
            // TODO:show error
        }
    }
}
