package com.ackerman.intermediatesubmission

import com.ackerman.intermediatesubmission.data.remote.response.RegisterResponse
import com.ackerman.intermediatesubmission.data.remote.response.ResponseLogin
import com.ackerman.intermediatesubmission.data.remote.response.Result

object DummyAuth {
    fun provideLoginResponse(): ResponseLogin = ResponseLogin(false,"ok",com.ackerman.intermediatesubmission.data.remote.response.Result("", "ACKRMN", "iykwim"))

    fun provideRegisterResponse(): RegisterResponse = RegisterResponse(false, "Ok")

}