package com.andreypaunov.twitterquery

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.andreypaunov.twitterquery.databinding.LayoutMapMarketItemBinding
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.twitter.sdk.android.core.models.Tweet

class CustomMarkerInfoWindowView(activity: FragmentActivity, val viewModel: TwitterQueryViewModel?, private val destinationId: Int) : GoogleMap.InfoWindowAdapter {

    private val layoutInflater = LayoutInflater.from(activity)
    private var binding = DataBindingUtil.inflate<LayoutMapMarketItemBinding>(layoutInflater, R.layout.layout_map_market_item, null, false)

    override fun getInfoContents(marker: Marker?): View? {
        val tweet = marker?.tag as Tweet

        binding.viewModel = viewModel
        binding.selectedTweet = tweet
        binding.destinationId = destinationId
        binding.tweetText.text = tweet.text
        binding.executePendingBindings()

        return binding.root
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

}