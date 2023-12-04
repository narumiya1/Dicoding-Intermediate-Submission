package com.ackerman.intermediatesubmission.data.view_ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostStoryViewModel(private val storyRepository: DataStoryRepository): ViewModel() {

    fun postStory(token: String, file: MultipartBody.Part, desc : RequestBody,
    latitude: Double?, longitude : Double?) =
        storyRepository.postStory(token, file, desc, latitude, longitude)

    fun getUser(): LiveData<UserModel> {
        return storyRepository.getCurrentUserData()
    }
}