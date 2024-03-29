package com.andreypaunov.twitterquery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentDetailBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment
import com.andreypaunov.twitterquery.utils.Utils

class DetailFragment : BaseFragment() {

    private var tweetId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val bundleArgs = DetailFragmentArgs.fromBundle(it)
            tweetId = bundleArgs.tweetId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        val tweet = viewModel.getTweet(tweetId)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.userAvatarUrl = tweet?.user?.profileImageUrl
        binding.extendedEntities = tweet?.extendedEntities
        binding.tweetId = tweetId

        return binding.root
    }

}