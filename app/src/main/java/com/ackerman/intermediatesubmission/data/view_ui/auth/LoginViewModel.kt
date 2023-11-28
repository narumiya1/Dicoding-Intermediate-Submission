package com.ackerman.intermediatesubmission.data.view_ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.repository.StoryaRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyaRepository: StoryaRepository) : ViewModel() {

    fun loginUser(email: String, password: String) = storyaRepository.loginUser(email, password)

    fun saveUser(userModel: UserModel){
        viewModelScope.launch {
            storyaRepository.saveDataUser(userModel)
        }
    }

    fun logout() {
        viewModelScope.launch {
            storyaRepository.logout()
        }
    }
}