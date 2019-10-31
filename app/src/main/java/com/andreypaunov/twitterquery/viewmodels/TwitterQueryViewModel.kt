package com.andreypaunov.twitterquery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.andreypaunov.twitterquery.models.UserLocation
import com.andreypaunov.twitterquery.repositories.TwitterQueryRepository
import com.twitter.sdk.android.core.models.Tweet

class TwitterQueryViewModel : ViewModel() {

    val twitterQueryRepository = TwitterQueryRepository()
    val twitterLoginResult = MutableLiveData<TwitterLoginResult>()
    val mapFragmentStarted = MutableLiveData<Boolean>()
    val destinationId = MutableLiveData<Int>()
    var userLocation = MutableLiveData<UserLocation>()

    fun tweetDetails(tweet: Tweet) {

    }

//    fun onTwitterCallbackReceived() {
//                    val session = TwitterCore.getInstance().sessionManager.activeSession
//                    val twitterAuthClient = TwitterAuthClient()
//
//                    twitterAuthClient.requestEmail(session, object: Callback<String>() {
//                        override fun success(result: Result<String>?) {
//                            if (result != null) {
//                                Log.d("====", result.data)
//                            }
//                        }
//
//                        override fun failure(exception: TwitterException?) {
//
//                        }
//                    })
//    }

}