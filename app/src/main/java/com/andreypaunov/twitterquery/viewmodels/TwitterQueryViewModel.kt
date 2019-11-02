package com.andreypaunov.twitterquery.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.andreypaunov.twitterquery.fragments.MapFragmentDirections
import com.andreypaunov.twitterquery.models.LoginResultModel
import com.andreypaunov.twitterquery.models.SelectedTweetModel
import com.andreypaunov.twitterquery.models.UserLocationModel
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode

class TwitterQueryViewModel : ViewModel() {

    var tweetsResultLiveData = MutableLiveData<Result<Search>?>()
    val twitterLoginResultLiveData = MutableLiveData<LoginResultModel>()
    val mapFragmentStartedLiveData = MutableLiveData<Boolean>()
    val navDirectionsLiveData = MutableLiveData<NavDirections>()
    var userLocationLiveData = MutableLiveData<UserLocationModel>()
    var likesLiveData = MutableLiveData<Int>()
    var retweetsLiveData = MutableLiveData<Int>()
    var selectedTweetLiveData = MutableLiveData<SelectedTweetModel>()

    lateinit var twitterApiClient: TwitterApiClient

    fun callbackReceived() {
        twitterApiClient = TwitterCore.getInstance().apiClient
    }

    fun tweetDetails(tweetId: Long) {
        checkTweetStatus(tweetId)
        navDirectionsLiveData.value = MapFragmentDirections.openTweetDetails(tweetId)
    }

    fun getTweets(query: String, userLocationModel: UserLocationModel, radius: Int) {
        val geocode = Geocode(userLocationModel.latitude, userLocationModel.longitude, radius, Geocode.Distance.KILOMETERS)

        twitterApiClient.searchService?.tweets(query, geocode, null, null, null, 100, null, 0, 0, false)?.enqueue(object : Callback<Search>() {
            override fun success(result: Result<Search>?) {
                tweetsResultLiveData.value = result
            }

            override fun failure(exception: TwitterException?) {
                // No-op
            }
        })
    }

    fun getTweet(tweetId: Long): Tweet? {
        val tweets = tweetsResultLiveData.value?.data?.tweets
        var foundTweet: Tweet? = null

        if (tweets != null) {
            for (tweet in tweets) {
                if (tweetId == tweet.id) {
                    foundTweet = tweet
                    break
                }
            }
        }

        return foundTweet
    }

    fun onLike(tweetId: Long) {
        twitterApiClient.favoriteService?.create(tweetId, true)?.enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>?) {
                if (result != null) {
                    selectedTweetLiveData.value = SelectedTweetModel(tweetId, true)
                }
            }

            override fun failure(exception: TwitterException?) {
                // No-op
                if (exception != null) {
                    onDislike(tweetId)
                }
            }
        })
    }

    fun onDislike(tweetId: Long) {
        twitterApiClient.favoriteService.destroy(tweetId, true).enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>?) {
                selectedTweetLiveData.value = SelectedTweetModel(tweetId, false)
            }

            override fun failure(exception: TwitterException?) {
                // No-op
            }
        })
    }

    fun onRetweet(tweetId: Long) {
        twitterApiClient.statusesService.retweet(tweetId, false).enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>?) {
                Log.d("====", "Retweet")
            }

            override fun failure(exception: TwitterException?) {
                // No-op
            }
        })
    }

    private fun checkTweetStatus(tweetId: Long) {
        twitterApiClient.statusesService.lookup(tweetId.toString(), false, false, false).enqueue(object : Callback<List<Tweet>>() {
            override fun success(result: Result<List<Tweet>>?) {
                if (result != null) {
                    for (tweet in result.data) {
                        if (tweet.id == tweetId) {
                            selectedTweetLiveData.value = SelectedTweetModel(tweetId, tweet.favorited)
                            break
                        }
                    }
                }
            }

            override fun failure(exception: TwitterException?) {

            }
        })
    }

}