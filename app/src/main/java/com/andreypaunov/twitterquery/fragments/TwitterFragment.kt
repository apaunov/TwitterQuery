package com.andreypaunov.twitterquery.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentTwitterBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class TwitterFragment : BaseFragment() {

    private lateinit var twitterLoginButton: TwitterLoginButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 12345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("====", "onCreate")

        activity?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("====", "onCreateView")

        val binding: FragmentTwitterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_twitter, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        twitterLoginButton = binding.twitterButtonId
        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                result?.let {
                    Log.d("====", "twitter callback")
                    binding.viewModel?.twitterQueryRepository?.callbackReceived()
                    requestLocationPermission()
                }
            }

            override fun failure(exception: TwitterException?) {
                exception?.message.let {
                    Log.d("====", it.toString())
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.twitterLoginResult?.observe(viewLifecycleOwner, Observer<TwitterLoginResult> {
            twitterLoginButton.onActivityResult(it.requestCode, it.resultCode, it.data)
        })
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
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION) }
        }
    }

}