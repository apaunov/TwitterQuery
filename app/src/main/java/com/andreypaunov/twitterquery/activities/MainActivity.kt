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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TwitterQueryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController = findNavController(R.id.nav_host_fragment)
        (binding.mainToolbar as Toolbar).setupWithNavController(navController)

        viewModel.mapFragmentStarted.observe(this, Observer<Boolean> {
            requestLocationPermission()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("====", "Activity onActivityResult")

        viewModel.twitterLoginResult.value = TwitterLoginResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MapFragment.REQUEST_LOCATION_PERMISSION -> {
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
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MapFragment.REQUEST_LOCATION_PERMISSION)
        }
    }
}
