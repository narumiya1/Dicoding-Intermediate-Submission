package com.ackerman.intermediatesubmission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ackerman.intermediatesubmission.data.remote.response.StoryResponse
import com.ackerman.intermediatesubmission.data.utils.ViewModelFactory
import com.ackerman.intermediatesubmission.data.view_ui.maaps.MapsViewModel
import com.ackerman.intermediatesubmission.data.view_ui.story.DetailStoryActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ackerman.intermediatesubmission.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mapsViewModel: MapsViewModel
    private val boundBuilder = LatLngBounds.builder()
    private var iteratorPage: Int = 0
    private var isStoryFirst: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iteratorPage = 1
        supportActionBar?.title = "Story Maps User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }

    private fun setupViewModel() {
        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, viewModelFactory)[MapsViewModel::class.java]
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupStory()
        myLocation()
    }


    private fun myLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                myLocation()
            }
        }


    private fun setupStory() {
        mapsViewModel.getUser().observe(this){story->
            val token = "Bearer " + story.token
            mapsViewModel.getLocationStory(token).observe(this) {
                when (it) {
                    is com.ackerman.intermediatesubmission.data.utils.Result.Loading -> {}
                    is com.ackerman.intermediatesubmission.data.utils.Result.Success -> showMarker(it.data.listStory)
                    is com.ackerman.intermediatesubmission.data.utils.Result.Error -> Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMarker(listStory: List<StoryResponse.StoryApp>) {
        listStory.forEach{
            val latLng = it.lon?.let { it1->it.lat?.let { it2 -> LatLng(it2, it1) } }
            val marker = latLng?.let {
                it1->
                MarkerOptions()
                    .position(it1)
                    .title("Lokasi user "+it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .alpha(0.7f)
                    .snippet(it.description)
            }?.let{ it2->
                mMap.addMarker(
                    it2
                )
            }
            latLng?.let { it1 -> CameraUpdateFactory.newLatLng(it1) }
                ?.let {
                        it2->mMap.moveCamera(it2) }


            if (latLng != null){
                boundBuilder.include(latLng)
            }

            marker?.tag = it
            mMap.setOnInfoWindowClickListener {
                val intent = Intent(this, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_DETAIL, it.tag as StoryResponse.StoryApp)
                }
                startActivity(intent)
            }
            if (!isStoryFirst) {
                latLng?.let { it1 -> CameraUpdateFactory.newLatLng(it1) }
                    ?.let { it2 ->
                        mMap.moveCamera(it2) }
                isStoryFirst = true
            }
        }

    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}