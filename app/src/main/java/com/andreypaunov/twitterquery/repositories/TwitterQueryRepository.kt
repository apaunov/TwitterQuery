package com.andreypaunov.twitterquery.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.services.params.Geocode

class TwitterQueryRepository {

    private var twitterApiClient = MutableLiveData<TwitterApiClient>()

    fun callbackReceived() {
        twitterApiClient.value = TwitterCore.getInstance().apiClient

        //TODO: Hard code for now
//        getTweets("#kitten", 5)
    }

    fun getTweets(query: String, radius: Int): ArrayList<Search> {
        var tweets = ArrayList<Search>()
//        val geocode = Geocode(1, 1, radius, Geocode.Distance.KILOMETERS)

        twitterApiClient.value?.searchService?.tweets(
            "twitterdev",
            null,
            null,
            null,
            null,
            20,
            null,
            0,
            0,
            false
        )
            ?.enqueue(object : Callback<Search>() {
                override fun success(result: Result<Search>?) {
                    Log.d("====", "")
                }

                override fun failure(exception: TwitterException?) {

                }
            })

        return tweets
    }

}