package com.andreypaunov.twitterquery.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.andreypaunov.twitterquery.application.VolleySingleton
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest

@BindingAdapter("app:userAvatar")
fun userAvatar(imageView: ImageView, imageUrl: String?) {

    val imageRequest = ImageRequest(imageUrl, Response.Listener {
        imageView.setImageBitmap(it)
    }, imageView.layoutParams.width, imageView.layoutParams.height, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, Response.ErrorListener {
        // No-op
    })

    VolleySingleton.getInstance(imageView.context.applicationContext).addToRequestQueue(imageRequest)
}