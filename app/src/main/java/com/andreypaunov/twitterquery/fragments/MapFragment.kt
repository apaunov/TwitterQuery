package com.andreypaunov.twitterquery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentMapBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment

class MapFragment: BaseFragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 12345
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMapBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel?.mapFragmentStarted?.value = true

        return binding.root
    }

}