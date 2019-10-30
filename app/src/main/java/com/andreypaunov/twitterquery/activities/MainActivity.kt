package com.andreypaunov.twitterquery.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andreypaunov.twitterquery.R
import com.andreypaunov.twitterquery.viewmodels.TwitterQueryViewModel
import com.andreypaunov.twitterquery.databinding.ActivityMainBinding
import com.andreypaunov.twitterquery.models.TwitterLoginResult

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TwitterQueryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController = findNavController(R.id.nav_host_fragment)
        (binding.mainToolbar as Toolbar).setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("====", "Activity onActivityResult")

        viewModel.twitterLoginResult.value = TwitterLoginResult(requestCode, resultCode, data)
    }
}
