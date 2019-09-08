package com.zepvalue.currentburritos

import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.zepvalue.currentburritos.api.RetrofitClient
import com.zepvalue.currentburritos.model.google.nearbySearch.NearbySearch
import com.zepvalue.currentburritos.model.others.Spot
import com.zepvalue.currentburritos.utility.Constants
import kotlinx.android.synthetic.main.activity_scroll.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlin.collections.ArrayList

class ScrollActivity : AppCompatActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val REQUEST_PERMISSION_LOCATION = 10
    lateinit var mLastLocation: Location
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var mProgressBar: ProgressBar
    private lateinit var connectivityManager: ConnectivityManager

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val PLACE_PICKER_REQUEST = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)

        if (!isNetworkConnected()) {
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }

        mProgressBar = findViewById(R.id.progressBar)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    lastLocation = location
                    updateLastLocation()
                }
                mProgressBar.setVisibility(View.GONE)
            }
        }

        createLocationRequest()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                updateLastLocation()

            } else {
                toast("Location is null")
            }
        }
    }


    private fun updateLastLocation() {
        val placesCall = RetrofitClient.googleMethods().getNearbySearch(
            lastLocation.latitude.toString() + "," + lastLocation.longitude.toString(),
            Constants.RADIUS,
            Constants.TYPE_BAR,
            Constants.KEYWORD,
            Constants.GOOGLE_API_KEY
        )

        placesCall.enqueue(object : Callback<NearbySearch> {
            override fun onFailure(call: Call<NearbySearch>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<NearbySearch>, response: Response<NearbySearch>
            ) {
                val nearbySearch = response.body()!!
                if (nearbySearch.status.equals("OK")) {
                    val spotList = ArrayList<Spot>()
                    for (resultItem in nearbySearch.results!!) {
                        val spot = Spot(
                            resultItem.name,
                            resultItem.geometry.location?.lat,
                            resultItem.geometry.location?.lng,
                            resultItem.vicinity,
                            resultItem.priceLevel,
                            resultItem.userRatings
                        )
                        spotList.add(spot)
                    }
                    rv_place_list.layoutManager = LinearLayoutManager(applicationContext)
                    rv_place_list.adapter = PlaceAdapter(spotList, applicationContext)
                } else {
                    toast(nearbySearch.status)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }
    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            null /* Looper */
        )
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        this@ScrollActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
        val networkInfo = connectivityManager.activeNetworkInfo //2
        return networkInfo != null && networkInfo.isConnected //3
    }
}
