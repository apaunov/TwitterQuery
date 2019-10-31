package com.andreypaunov.twitterquery.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.ActivityMainBinding
import com.andreypaunov.twitterquery.fragments.MapFragment
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
//    private lateinit var locationCallback: LocationCallback
    private lateinit var locationSettingsRequest: LocationSettingsRequest

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 12345
        private const val UPDATE_INTERVAL: Long = 3600000
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TwitterQueryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()
        buildLocationSettingsRequest()

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController = findNavController(R.id.nav_host_fragment)
        (binding.mainToolbar as Toolbar).setupWithNavController(navController)

        viewModel.mapFragmentStarted.observe(this, Observer<Boolean> {
            requestLocationPermission()
        })

        viewModel.destinationId.observe(this, Observer {
            navigate(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("====", "Activity onActivityResult")

        viewModel.twitterLoginResult.value = TwitterLoginResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            Log.d("====", "lat: ${it.latitude}; long: ${it.longitude}")
                        }
                    }
                }
            }
        }
    }

    // Helper functions

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun navigate(actionId: Int) {
        navController.navigate(actionId)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun buildLocationSettingsRequest() {
        locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    }

//    private fun createLocationCallback() {
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                super.onLocationResult(locationResult)
//
//                if (locationResult != null) {
//                    Log.d("====", "${locationResult.lastLocation.latitude}; ${locationResult.lastLocation.longitude}")
//                }
//            }
//        }
//    }
}
