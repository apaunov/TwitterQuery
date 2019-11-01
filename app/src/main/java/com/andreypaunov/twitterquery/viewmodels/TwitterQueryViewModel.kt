package com.andreypaunov.twitterquery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.andreypaunov.twitterquery.fragments.MapFragmentDirections
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.andreypaunov.twitterquery.models.UserLocation
import com.andreypaunov.twitterquery.repositories.TwitterQueryRepository

class TwitterQueryViewModel : ViewModel() {

    val twitterQueryRepository = TwitterQueryRepository()
    val twitterLoginResult = MutableLiveData<TwitterLoginResult>()
    val mapFragmentStarted = MutableLiveData<Boolean>()
    val navDirections = MutableLiveData<NavDirections>()
    var userLocation = MutableLiveData<UserLocation>()

    fun tweetDetails(tweetId: Long) {
        navDirections.value = MapFragmentDirections.openTweetDetails(tweetId)
    }

}