package com.ackerman.intermediatesubmission.data.view_ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val dataStoryRepository: DataStoryRepository) : ViewModel() {

    fun loginUser(email: String, password: String) = dataStoryRepository.loginUser(email, password)

    fun saveUser(userModel: UserModel){
        viewModelScope.launch {
            dataStoryRepository.saveDataUser(userModel)
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoryRepository.logout()
        }
    }
}