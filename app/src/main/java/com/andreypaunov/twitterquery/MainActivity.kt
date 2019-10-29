package com.andreypaunov.twitterquery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.andreypaunov.twitterquery.databinding.ActivityMainBinding
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class MainActivity : AppCompatActivity() {

    private lateinit var twitterLoginButton: TwitterLoginButton

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CustomViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        twitterLoginButton = binding.twitterButtonId
        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                if (result != null) {
                    Log.d("====", result.data.userName)
                }
            }

            override fun failure(exception: TwitterException?) {
                if (exception?.message != null) {
                    Log.d("====", exception.message.toString())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("====", "onActivityResult")

        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }
}
