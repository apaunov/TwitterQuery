package com.andreypaunov.twitterquery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.adapters.viewholders.TweetImageHolder
import com.twitter.sdk.android.core.models.MediaEntity

class TweetImageAdapter(private val media: MutableList<MediaEntity>?) : RecyclerView.Adapter<TweetImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetImageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return TweetImageHolder(DataBindingUtil.inflate(layoutInflater, R.layout.layout_tweet_image, parent, false))
    }

    override fun onBindViewHolder(holder: TweetImageHolder, position: Int) {
        holder.bindImage(media?.get(position)?.mediaUrl)
    }

    override fun getItemCount(): Int {
        return media?.size ?: 0
    }

}