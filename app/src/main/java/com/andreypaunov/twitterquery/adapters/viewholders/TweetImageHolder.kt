package com.andreypaunov.twitterquery.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.andreypaunov.twitterquery.databinding.LayoutTweetImageBinding
import com.andreypaunov.twitterquery.utils.Utils

class TweetImageHolder(private val layoutTweetImageBinding: LayoutTweetImageBinding): RecyclerView.ViewHolder(layoutTweetImageBinding.root) {

    fun bindImage(imageUrl: String?) {
        Utils.downloadImage(layoutTweetImageBinding.tweetImage, imageUrl)
    }

}