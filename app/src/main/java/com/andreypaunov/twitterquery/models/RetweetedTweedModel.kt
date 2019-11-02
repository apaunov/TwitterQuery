package com.andreypaunov.twitterquery.models

data class RetweetedTweedModel(val tweetId: Long, val retweeted: Boolean, val retweetedCount: Int)