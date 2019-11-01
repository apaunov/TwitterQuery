package com.andreypaunov.twitterquery.fragments.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel

abstract class BaseFragment: Fragment() {

    protected val viewModel by lazy {
        activity?.let {
            ViewModelProviders.of(it).get(TwitterQueryViewModel::class.java)
        }
    }

    protected fun hideKeyboard(activity: FragmentActivity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

}