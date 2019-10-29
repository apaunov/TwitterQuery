package com.andreypaunov.twitterquery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class CustomViewModel(application: Application) : AndroidViewModel(application) {

    fun onTwitterCallback(twitterLoginButton: TwitterLoginButton) {

    }

}