package com.ackerman.intermediatesubmission.data.view_ui.maaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository

class MapsViewModel (private val repository: DataStoryRepository) : ViewModel() {

    fun getLocationStory(token: String) = repository.getAllzStoryWithLocation(token)

    fun getUser(): LiveData<UserModel> {
        return repository.getCurrentUserData()
    }
}