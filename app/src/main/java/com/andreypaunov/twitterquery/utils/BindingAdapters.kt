package com.andreypaunov.twitterquery.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreypaunov.twitterquery.adapters.TweetImageAdapter
import com.twitter.sdk.android.core.models.MediaEntity
import com.twitter.sdk.android.core.models.TweetEntities

@BindingAdapter("app:userAvatar")
fun userAvatar(imageView: ImageView, imageUrl: String?) {

    Utils.downloadImage(imageView, imageUrl)

}

@BindingAdapter("app:adapterData")
fun setRecyclerViewData(recyclerView: RecyclerView, extendedEntities: TweetEntities) {
    val media = extendedEntities.media

    if (media.isNotEmpty() && media.first().type.equals("photo", true)) {
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = TweetImageAdapter(media)
    }
}

@BindingAdapter("app:videoData")
fun setVideoData() {

}
