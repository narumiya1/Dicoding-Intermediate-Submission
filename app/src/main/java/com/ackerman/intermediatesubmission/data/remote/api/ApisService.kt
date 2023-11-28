package com.ackerman.intermediatesubmission.data.remote.api

import com.ackerman.intermediatesubmission.data.remote.response.LoginRequest
import com.ackerman.intermediatesubmission.data.remote.response.RegisterRequest
import com.ackerman.intermediatesubmission.data.remote.response.RegisterResponse
import com.ackerman.intermediatesubmission.data.remote.response.ResponseLogin
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

}