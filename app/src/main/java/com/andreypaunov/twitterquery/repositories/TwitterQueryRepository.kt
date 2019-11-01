package com.andreypaunov.twitterquery.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.andreypaunov.twitterquery.models.UserLocation
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode

class TwitterQueryRepository {

    // TODO: Maybe move this to the viewModel??

    var tweetsResult = MutableLiveData<Result<Search>?>()

    private lateinit var twitterApiClient: TwitterApiClient

    fun callbackReceived() {
        twitterApiClient = TwitterCore.getInstance().apiClient
    }

    fun getTweets(query: String, userLocation: UserLocation, radius: Int) {
        val geocode = Geocode(userLocation.latitude, userLocation.longitude, radius, Geocode.Distance.KILOMETERS)

        twitterApiClient.searchService?.tweets(query, geocode, null, null, null, 100, null, 0, 0, false)?.enqueue(object : Callback<Search>() {
            override fun success(result: Result<Search>?) {
                tweetsResult.value = result
            }

            override fun failure(exception: TwitterException?) {
                // No-op
            }
        })
    }

}