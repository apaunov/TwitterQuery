package com.andreypaunov.twitterquery.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreypaunov.twitterquery.adapters.TweetImageAdapter
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
fun setVideoData(videoView: VideoView, extendedEntities: TweetEntities) {
    val media = extendedEntities.media
    val firstEntityType = media.first().type

    if (media.isNotEmpty() && firstEntityType.equals("video", true) || firstEntityType.equals("animated_gif", true)) {
        var videoPath = ""

        for (variant in media.first().videoInfo.variants) {
            if (variant.contentType.equals("video/mp4", true)) {
                videoPath = variant.url
                break
            }
        }

        if (videoPath.isNotEmpty()) {
            videoView.visibility = View.VISIBLE

            val uri = Uri.parse(videoPath)
            val mediaController = MediaController(videoView.context)
            mediaController.setAnchorView(videoView)

            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.requestFocus()
            videoView.start()
        }
    }
}
