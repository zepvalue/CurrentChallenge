package com.zepvalue.currentburritos


import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zepvalue.currentburritos.model.others.Spot
import android.os.Parcelable
import com.zepvalue.currentburritos.utility.genDescription
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var spot: Spot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        spot = intent.getParcelableExtra<Parcelable>("EXTRA_MARKER") as Spot
        tv_address.setText(spot.vicinity)
        tv_ratings.setText("$".repeat(spot.price) + " • " + genDescription(spot.userRating))
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val marker = LatLng(spot.lat!!, spot.lng!!)

        mMap.addMarker(MarkerOptions().position(marker).title(spot.name))
        val zoomLevel = 15.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel))
    }
}

