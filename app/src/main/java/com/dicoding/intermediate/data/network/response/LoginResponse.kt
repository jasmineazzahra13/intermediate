package com.dicoding.intermediate.data.network.response

import com.dicoding.intermediate.data.md.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    val error: Boolean,
    val message: String,
    @SerializedName("loginResult")
    val data: User
)