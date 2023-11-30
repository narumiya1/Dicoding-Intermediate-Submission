package com.ackerman.intermediatesubmission.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.local.UserPreference
import com.ackerman.intermediatesubmission.data.remote.api.ApiService
import com.ackerman.intermediatesubmission.data.remote.response.*
import com.ackerman.intermediatesubmission.data.view_ui.adapter.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DataStoryRepository (private val userPreference: UserPreference, private val apiService: ApiService) {


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

    fun registerUser(name: String, email: String, password: String): LiveData<com.ackerman.intermediatesubmission.data.utils.Result<RegisterResponse>> = liveData {
        emit(com.ackerman.intermediatesubmission.data.utils.Result.Loading)
        try {
            val response = apiService.userRegister(RegisterRequest(name, email, password))
            emit(com.ackerman.intermediatesubmission.data.utils.Result.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(com.ackerman.intermediatesubmission.data.utils.Result.Error(e.message.toString()))
        }
    }
    fun getAllzStory(): LiveData<PagingData<StoryResponse.StoryApp>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }

    fun getCurrentUserData(): LiveData<UserModel> {
        return userPreference.getUserData().asLiveData()

    }

    fun postStory(token: String, file: MultipartBody.Part, description: RequestBody):
            LiveData<com.ackerman.intermediatesubmission.data.utils.Result<PostStoryResponse>> = liveData {

        emit(com.ackerman.intermediatesubmission.data.utils.Result.Loading)
        try {
            val response = apiService.addStory(token, file, description)
            emit(com.ackerman.intermediatesubmission.data.utils.Result.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(com.ackerman.intermediatesubmission.data.utils.Result.Error(e.message.toString()))
        }
    }
    companion object {
        @Volatile
        private var instance: DataStoryRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): DataStoryRepository =
            instance ?: synchronized(this) {
                instance ?: DataStoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}