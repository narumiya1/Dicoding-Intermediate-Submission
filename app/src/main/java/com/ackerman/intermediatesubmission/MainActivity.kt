package com.ackerman.intermediatesubmission

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ackerman.intermediatesubmission.data.utils.ViewModelFactory
import com.ackerman.intermediatesubmission.data.view_ui.adapter.LoadingAdapter
import com.ackerman.intermediatesubmission.data.view_ui.adapter.StoryAdapter
import com.ackerman.intermediatesubmission.data.view_ui.auth.LoginActivity
import com.ackerman.intermediatesubmission.data.view_ui.auth.LoginViewModel
import com.ackerman.intermediatesubmission.data.view_ui.story.PostStoryActivity
import com.ackerman.intermediatesubmission.data.view_ui.story.StoryViewModel
import com.ackerman.intermediatesubmission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var storyAdapter: StoryAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        supportActionBar?.hide()

        mainBinding.rvStory.layoutManager = LinearLayoutManager(this)
        mainBinding.rvStory.setHasFixedSize(true)

        viewModelSetUp()


        storyAdapter = StoryAdapter()

        storyViewModel.getCurrentUser().observe(this) {
            if (it.isLogin) {
                getStory()
                mainBinding.imgLogo.setText("Hello, ${it.name}")
                showProgress(false)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        mainBinding.btnAddStory.setOnClickListener {
            startActivity(Intent(this, PostStoryActivity::class.java))
        }

        mainBinding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        mainBinding.btnLogout.setOnClickListener {
            logout()
        }

        mainBinding.btnMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

    }

    private fun viewModelSetUp() {
        viewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        storyViewModel = ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]
    }

    private fun showProgress(isLoading: Boolean) {
        if (isLoading) {
            mainBinding.progressCircular.visibility = View.VISIBLE
        } else {
            mainBinding.progressCircular.visibility = View.GONE
        }
    }

    private fun getStory() {
        mainBinding.rvStory.adapter = storyAdapter.withLoadStateFooter(footer = LoadingAdapter {
            storyAdapter.retry()
        })
        storyViewModel.getStory().observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showProgress(false)
        }
    }



    private fun logout() {
        loginViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}