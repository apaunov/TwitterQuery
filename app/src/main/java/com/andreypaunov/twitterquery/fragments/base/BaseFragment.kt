package com.andreypaunov.twitterquery.fragments.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel

abstract class BaseFragment: Fragment() {

    protected val viewModel by lazy {
        activity?.let {
            ViewModelProviders.of(it).get(TwitterQueryViewModel::class.java)
        }
    }

}