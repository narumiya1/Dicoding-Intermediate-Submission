package com.ackerman.intermediatesubmission.data.remote.api

import com.ackerman.intermediatesubmission.data.remote.response.*
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun userLogin(
        @Body request: LoginRequest
    ): ResponseLogin

    @POST("register")
    suspend fun userRegister(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

}