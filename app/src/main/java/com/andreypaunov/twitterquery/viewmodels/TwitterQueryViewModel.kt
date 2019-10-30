package com.andreypaunov.twitterquery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.andreypaunov.twitterquery.repositories.TwitterQueryRepository

class TwitterQueryViewModel : ViewModel() {

    val twitterLoginResult = MutableLiveData<TwitterLoginResult>()
    val twitterQueryRepository = TwitterQueryRepository()
    val mapFragmentStarted = MutableLiveData<Boolean>()

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