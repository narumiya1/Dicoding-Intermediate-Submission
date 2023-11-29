package com.ackerman.intermediatesubmission.data.view_ui.auth

import androidx.lifecycle.ViewModel
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository

class RegisterViewModel (private val storyRepository: DataStoryRepository): ViewModel() {

    fun userRegister(name: String, email: String, password: String) =
        storyRepository.registerUser(name, email, password)

}