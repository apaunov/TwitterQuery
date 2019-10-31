package com.andreypaunov.twitterquery.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentMapBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment: BaseFragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var mapViewBundle: Bundle

    companion object {
        const val MAP_KEY = "MAP_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMapBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
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

            val ny = LatLng(40.7143528, -74.0059731)
            val marketOptions = MarkerOptions()
            marketOptions.position(ny)

            googleMap.addMarker(marketOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        Log.d("====", "onCreateOptionsMenu")

        inflater.inflate(R.menu.options_map_menu, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchItem.apply {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("====", "Search submit")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("====", "Query text changed")
                return true
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

}