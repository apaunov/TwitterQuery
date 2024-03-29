package com.andreypaunov.twitterquery.application

import android.app.Application
import android.util.Log
import com.andreypaunov.twitterquery.R
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class TwitterQueryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config: TwitterConfig = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(TwitterAuthConfig(resources.getString(R.string.twitter_api_key), resources.getString(R.string.twitter_api_secret_key)))
            .debug(true)
            .build()

        Twitter.initialize(config)
    }
}