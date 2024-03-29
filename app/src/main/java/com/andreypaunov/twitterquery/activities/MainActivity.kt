package com.andreypaunov.twitterquery.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.andreypaunov.twitterquery.BuildConfig
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.ActivityMainBinding
import com.andreypaunov.twitterquery.models.LoginResultModel
import com.andreypaunov.twitterquery.models.UserLocationModel
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
        private const val UPDATE_FASTEST_INTERVAL: Long = 2000
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

        viewModel.mapFragmentStartedLiveData.observe(this, Observer {
            requestPermissions()
        })

        viewModel.navDirectionsLiveData.observe(this, Observer {
            navigate(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_ACTIVITY_SETTINGS_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        startLocationUpdates()
                    }
                }
            }
        }

        viewModel.twitterLoginResultLiveData.value = LoginResultModel(requestCode, resultCode, data)
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

    private fun navigate(navDirections: NavDirections) {
        navController.navigate(navDirections)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL
            fastestInterval = UPDATE_FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun buildLocationSettingsRequest() {
        locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (shouldProvideRationale) {
            showSnackbar(R.string.permission_rational, R.string.permission_allow, View.OnClickListener {
                ActivityCompat.requestPermissions(this@MainActivity, Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION_CODE)
            })
        } else {
            ActivityCompat.requestPermissions(this@MainActivity, Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION_CODE)
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    viewModel.userLocationLiveData.value = UserLocationModel(it.latitude, it.longitude)
                }.addOnFailureListener {
                    // No-op
                }

            }.addOnFailureListener {
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED         -> {
                        try {
                            val resolvableApiException = it as ResolvableApiException
                            resolvableApiException.startResolutionForResult(this@MainActivity, REQUEST_CHECK_ACTIVITY_SETTINGS_CODE)
                        } catch (e: IntentSender.SendIntentException) {
                            // No-op
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // No-op
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
