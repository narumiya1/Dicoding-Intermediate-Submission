package com.ackerman.intermediatesubmission.data.view_ui.auth

import androidx.lifecycle.ViewModel
import com.ackerman.intermediatesubmission.data.repository.StoryaRepository

class RegisterViewModel (private val storyRepository: StoryaRepository): ViewModel() {

    fun userRegister(name: String, email: String, password: String) =
        storyRepository.registerUser(name, email, password)

}