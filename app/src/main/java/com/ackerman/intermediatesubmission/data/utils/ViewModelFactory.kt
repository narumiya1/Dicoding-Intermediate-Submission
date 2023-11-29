package com.ackerman.intermediatesubmission.data.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ackerman.intermediatesubmission.data.di.Injection
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import com.ackerman.intermediatesubmission.data.view_ui.auth.LoginViewModel
import com.ackerman.intermediatesubmission.data.view_ui.auth.RegisterViewModel
import com.ackerman.intermediatesubmission.data.view_ui.story.StoryViewModel

class ViewModelFactory (private val repository: DataStoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}