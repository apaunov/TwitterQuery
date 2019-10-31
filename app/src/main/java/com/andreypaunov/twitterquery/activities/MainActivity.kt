package com.andreypaunov.twitterquery.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andreypaunov.twitterquery.BuildConfig
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.ActivityMainBinding
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.andreypaunov.twitterquery.models.UserLocation
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var settingsClient: SettingsClient

    companion object {
        private const val REQUEST_CHECK_ACTIVITY_SETTINGS_CODE = 1000
        private const val REQUEST_LOCATION_PERMISSION_CODE = 1234
        private const val UPDATE_INTERVAL: Long = 20000
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TwitterQueryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        settingsClient = LocationServices.getSettingsClient(this)

        createLocationRequest()
        buildLocationSettingsRequest()

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)

        viewModel.mapFragmentStarted.observe(this, Observer<Boolean> {
            requestPermissions()
        })

        viewModel.destinationId.observe(this, Observer {
            navigate(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("====", "Activity onActivityResult")

        when (requestCode) {
            REQUEST_CHECK_ACTIVITY_SETTINGS_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startLocationUpdates()
                    }
                }
            }
        }

        viewModel.twitterLoginResult.value = TwitterLoginResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_CODE -> {
                when {
                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        startLocationUpdates()
                    }

                    else                                                 -> {
                        showSnackbar(R.string.permission_denied, R.string.permission_settings, View.OnClickListener {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

                            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            startActivity(intent)
                        })
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Helper functions

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

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (shouldProvideRationale) {
            Log.i("====", "Request Permissions: Rationale")
            showSnackbar(R.string.permission_rational, R.string.permission_allow, View.OnClickListener {
                ActivityCompat.requestPermissions(this@MainActivity, Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION_CODE)
            })
        } else {
            Log.i("====", "Request Permissions")
            ActivityCompat.requestPermissions(this@MainActivity, Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION_CODE)
        }
    }

    private fun startLocationUpdates() {
        Log.i("====", "Start location updates")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("====", "Permission granted")
            settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener {
                Log.i("====", "checkLocationSettingsTask.addOnSuccessListener")

                fusedLocationClient.lastLocation.addOnSuccessListener {
                    Log.d("====", "lat: ${it.latitude}; long: ${it.longitude}")
                    viewModel.userLocation.value = UserLocation(it.latitude, it.longitude)
                }.addOnFailureListener {
                    Log.i("====", "Could not retrieve location at this time")
                }

            }.addOnFailureListener {
                Log.i("====", "checkLocationSettingsTask.addOnFailureListener")
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED         -> {
                        try {
                            Log.i("====", "LocationSettingsStatusCode RESOLUTION_REQUIRED")
                            val resolvableApiException = it as ResolvableApiException
                            resolvableApiException.startResolutionForResult(this@MainActivity, REQUEST_CHECK_ACTIVITY_SETTINGS_CODE)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.i("====", "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.i("====", "LocationSettingsStatusCode SETTINGS_CHANGE_UNAVAILABLE")
                    }
                }
            }
        } else {
            requestPermissions()
        }
    }

    private fun showSnackbar(barTextId: Int, actionTextId: Int, listener: View.OnClickListener) {
        Snackbar.make(findViewById(android.R.id.content), getString(barTextId), Snackbar.LENGTH_INDEFINITE).setAction(getString(actionTextId), listener).show()
    }
}
