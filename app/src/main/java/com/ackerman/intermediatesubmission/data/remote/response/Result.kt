package com.ackerman.intermediatesubmission.data.remote.response


import com.google.gson.annotations.SerializedName

data class Result(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String,
)