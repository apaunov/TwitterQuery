package com.andreypaunov.twitterquery.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentMapBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet


class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mapViewBundle: Bundle
    private lateinit var binding: FragmentMapBinding

    private var googleMap: GoogleMap? = null

    companion object {
        const val MAP_KEY = "MAP_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel?.mapFragmentStarted?.value = true

        savedInstanceState?.let {
            mapViewBundle = it.getBundle(MAP_KEY)!!
        }

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.twitterQueryRepository?.tweetsResult?.observe(viewLifecycleOwner, Observer {
            displayTweets(it)
        })

        viewModel?.userLocation?.observe(viewLifecycleOwner, Observer {
            val currentLocation = LatLng(it.latitude, it.longitude)

            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        })
    }

    override fun onStart() {
        super.onStart()

        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()

        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()

        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()

        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()

        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            googleMap = map
            googleMap!!.isMyLocationEnabled = true
            googleMap!!.setOnMarkerClickListener {
                activity?.let { it1 -> hideKeyboard(it1) }
                displayMarketInfo(it)

                return@setOnMarkerClickListener false
            }
            googleMap!!.setOnMapClickListener {
                activity?.let { it1 -> hideKeyboard(it1) }
                dismissMarkerInfo()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.options_map_menu, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchItem.apply {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                googleMap?.clear()

                val userLocation = viewModel?.userLocation?.value

                if (query != null && userLocation != null) {
                    viewModel?.twitterQueryRepository?.getTweets(query, userLocation, 5)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var bundle = outState.getBundle(MAP_KEY)

        if (bundle == null) {
            bundle = Bundle()
            outState.putBundle(MAP_KEY, bundle)
        }

        mapView.onSaveInstanceState(bundle)
    }

    // Helper functions

    private fun displayTweets(result: Result<Search>?) {
        if (result != null) {
            for (tweet in result.data.tweets) {
                val coordinates = tweet.coordinates

                coordinates?.let {
                    val markerOptions = MarkerOptions()
                    markerOptions.position(LatLng(coordinates.latitude, coordinates.longitude))

                    val marker = googleMap?.addMarker(markerOptions)
                    marker?.tag = tweet
                }
            }
        }
    }

    private fun displayMarketInfo(marker: Marker) {
        val tweet = marker.tag as Tweet
        binding.tweetId = tweet.id
        binding.tweetText.text = tweet.text
        binding.markerInfoCard.visibility = View.VISIBLE
    }

    private fun dismissMarkerInfo() {
        binding.markerInfoCard.visibility = View.GONE
    }
}