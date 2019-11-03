package com.andreypaunov.twitterquery.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.andreypaunov.twitterquery.application.VolleySingleton
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest


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

    fun getDisplayPoint(context: Context?): Point {
        val display = (context as FragmentActivity).windowManager.defaultDisplay
        val pointSize = Point()
        display.getSize(pointSize)

        return pointSize
    }

}