package com.luckcheese.b2w

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.luckcheese.b2w.databinding.ActivityMapsBinding
import com.luckcheese.b2w.services.MapService

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    PlaceSelectionListener,
    View.OnClickListener
{

    private val mapService = MapService()

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupMaps()
        setupSearch()
    }

    private fun setupView() {
        binding.myLocationBtn.setOnClickListener(this)
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

    // ----- OnMapReadyCallback -----

    override fun onMapReady(googleMap: GoogleMap) {
        mapService.map = googleMap
    }

    // ----- PlaceSelectionListener -----

    override fun onPlaceSelected(place: Place) {
        mapService.selectedPlace = place
    }

    override fun onError(status: Status) {
        if (status != Status.RESULT_CANCELED) {
            // TODO:show error
        }
    }

    // ----- View.OnClickListener -----

    override fun onClick(view: View) {
        when(view) {

        }
    }
}
