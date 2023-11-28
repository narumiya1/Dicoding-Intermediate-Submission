package com.ackerman.intermediatesubmission.data.repository

import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.local.UserPreference
import com.ackerman.intermediatesubmission.data.remote.api.ApiService
import com.ackerman.intermediatesubmission.data.remote.response.LoginRequest
import com.ackerman.intermediatesubmission.data.remote.response.ResponseLogin

class StoryaRepository (private val userPreference: UserPreference, private val apiService: ApiService) {


    fun loginUser(email:String, password:String) : LiveData<com.ackerman.intermediatesubmission.data.utils.Result<ResponseLogin>> = liveData{
        emit(com.ackerman.intermediatesubmission.data.utils.Result.Loading)
        try {
            val response = apiService.userLogin(LoginRequest(email, password))
            emit(com.ackerman.intermediatesubmission.data.utils.Result.Success(response))

        }catch (e: Exception){
            Log.d("Login Fail", e.message.toString())
            emit(com.ackerman.intermediatesubmission.data.utils.Result.Error(e.message.toString()))

        }
    }

    suspend fun saveDataUser(userModel: UserModel){
        userPreference.saveUserData(userModel)
    }

    suspend fun login() {
        userPreference.login()
    }

    suspend fun logout() {
        userPreference.logout()
    }



    companion object {
        @Volatile
        private var instance: StoryaRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): StoryaRepository =
            instance ?: synchronized(this) {
                instance ?: StoryaRepository(preferences, apiService)
            }.also { instance = it }
    }
}