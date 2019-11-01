package com.andreypaunov.twitterquery.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.andreypaunov.twitterquery.application.VolleySingleton
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.twitter.sdk.android.core.models.*
import com.twitter.sdk.android.core.models.MediaEntity
import java.util.*
import kotlin.collections.ArrayList


object Utils {

    fun downloadImage(imageView: ImageView, imageUrl: String?) {
        imageUrl?.let {
            val imageRequest = ImageRequest(it, Response.Listener { bitmap ->
                imageView.setImageBitmap(bitmap)
            }, imageView.layoutParams.width, imageView.layoutParams.height, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, Response.ErrorListener {
                // No-op
            })

            VolleySingleton.getInstance(imageView.context.applicationContext).addToRequestQueue(imageRequest)
        }
    }

    // HARD CODE

    fun createMultiplePhotosTweet(count: Int, tweet: Tweet): Tweet {
        val mediaEntities = ArrayList<MediaEntity>()

        for (x in 0 until count) {
            val photoEntity = MediaEntity("", "", "", 0, 0, 0L, null, "https://pbs.twimg.com/media/DOhM30VVwAEpIHq.jpg", "", createMediaEntitySizes(200, 200), 0L, null, "photo", null, "")
            mediaEntities.add(photoEntity)
        }

        val entities = TweetEntities(null, null, mediaEntities, null, null)
        return TweetBuilder().setId(tweet.id).setUser(tweet.user).setText(tweet.text).setCreatedAt(tweet.createdAt).setEntities(entities).setExtendedEntities(entities).build()
    }

    private fun createMediaEntitySizes(width: Int, height: Int): MediaEntity.Sizes {
        val medium = MediaEntity.Size(width, height, "fit")
        return MediaEntity.Sizes(null, null, medium, null)
    }

    fun createVideoTweet(tweet: Tweet): Tweet {
        val variant = VideoInfo.Variant(2176000, "video/mp4", "https://video.twimg.com/ext_tw_video/869317980307415040/pu/vid/720x1280/octt5pFbISkef8RB.mp4")
        val videoInfo = VideoInfo(Collections.emptyList(), 10704, Collections.singletonList(variant))
        val videoEntity = MediaEntity("", "", "", 0, 0, 0L, null, "", "", createMediaEntitySizes(200, 200), 0L, null, "video", videoInfo, "")

        val entities = TweetEntities(null, null, Collections.singletonList(videoEntity), null, null)
        return TweetBuilder().setId(tweet.id).setUser(tweet.user).setText(tweet.text).setCreatedAt(tweet.createdAt).setEntities(entities).setExtendedEntities(entities).build()
    }

}