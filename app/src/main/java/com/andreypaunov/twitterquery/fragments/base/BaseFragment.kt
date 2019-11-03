package com.andreypaunov.twitterquery.fragments.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel

abstract class BaseFragment: Fragment() {

    protected val viewModel: TwitterQueryViewModel by activityViewModels()

    protected fun hideKeyboard(activity: FragmentActivity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

}