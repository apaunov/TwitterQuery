package com.andreypaunov.twitterquery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.andreypaunov.twitterquery.databinding.ActivityMainBinding
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
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

                    val session = TwitterCore.getInstance().sessionManager.activeSession
                    val twitterAuthClient = TwitterAuthClient()

                    twitterAuthClient.requestEmail(session, object: Callback<String>() {
                        override fun success(result: Result<String>?) {
                            if (result != null) {
                                Log.d("====", result.data)
                            }
                        }

                        override fun failure(exception: TwitterException?) {

                        }
                    })

                    val twitterApiClient = TwitterCore.getInstance().apiClient

                    val array = twitterApiClient.searchService.tweets("twitterdev", null, null, null, null, 20, null, 0, 0, false)


                    Log.d("====", array)
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
