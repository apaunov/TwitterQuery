package com.andreypaunov.twitterquery.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentTwitterBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment
import com.andreypaunov.twitterquery.models.TwitterLoginResult
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class TwitterFragment : BaseFragment() {

    private lateinit var twitterLoginButton: TwitterLoginButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentTwitterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_twitter, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        twitterLoginButton = binding.twitterButtonId
        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                result?.let {
                    Log.d("====", "twitter callback")
                    binding.viewModel?.twitterQueryRepository?.callbackReceived()
                    binding.viewModel?.destinationId?.value = TwitterFragmentDirections.openMap().actionId
                }
            }

            override fun failure(exception: TwitterException?) {
                exception?.message.let {
                    Log.d("====", it.toString())
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel?.twitterLoginResult?.observe(viewLifecycleOwner, Observer<TwitterLoginResult> {
            twitterLoginButton.onActivityResult(it.requestCode, it.resultCode, it.data)
        })
    }

}