package com.smartcampus.api.model

import com.google.gson.annotations.SerializedName

data class RemoteResponse<T>(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("message")
    val message: String = "",
    
    @SerializedName("data")
    val data: T? = null
) 