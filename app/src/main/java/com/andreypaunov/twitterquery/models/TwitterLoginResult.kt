package com.andreypaunov.twitterquery.models

import android.content.Intent

data class TwitterLoginResult(val requestCode: Int, val resultCode: Int, val data: Intent?)