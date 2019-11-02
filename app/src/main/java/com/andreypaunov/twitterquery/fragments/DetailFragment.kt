package com.andreypaunov.twitterquery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.databinding.FragmentDetailBinding
import com.andreypaunov.twitterquery.fragments.base.BaseFragment
import com.andreypaunov.twitterquery.models.SelectedTweetModel

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
        var tweet = viewModel?.getTweet(tweetId)

        //TODO: HARD CODE
//        tweet = tweet?.let { Utils.createMultiplePhotosTweet(4, it) }
//        tweet = tweet?.let { Utils.createVideoTweet(it) }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.userAvatarUrl = tweet?.user?.profileImageUrl
        binding.extendedEntities = tweet?.extendedEntities
        binding.tweetId = tweetId

        return binding.root
    }

}