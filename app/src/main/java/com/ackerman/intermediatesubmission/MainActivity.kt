package com.ackerman.intermediatesubmission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ackerman.intermediatesubmission.data.utils.ViewModelFactory
import com.ackerman.intermediatesubmission.data.view_ui.adapter.LoadingAdapter
import com.ackerman.intermediatesubmission.data.view_ui.adapter.StoryAdapter
import com.ackerman.intermediatesubmission.data.view_ui.auth.LoginActivity
import com.ackerman.intermediatesubmission.data.view_ui.auth.LoginViewModel
import com.ackerman.intermediatesubmission.data.view_ui.story.StoryViewModel
import com.ackerman.intermediatesubmission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.rvStory.layoutManager = LinearLayoutManager(this)
        mainBinding.rvStory.setHasFixedSize(true)

        viewModelSetUp()


        storyAdapter = StoryAdapter()

        storyViewModel.getCurrentUser().observe(this) {
            if (it.isLogin) {
                getStory()
                showProgress(false)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menus, menu)
        return true
    }

    private fun logout() {
        loginViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}