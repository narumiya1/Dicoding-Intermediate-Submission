package com.ackerman.intermediatesubmission.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostStoryResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)