package com.dicoding.intermediate.data.network.services

import com.dicoding.intermediate.data.network.request.LoginReq
import com.dicoding.intermediate.data.network.request.RegisterReq
import com.dicoding.intermediate.data.network.response.LoginResponse
import com.dicoding.intermediate.data.network.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServ {

    @POST("login")
    suspend fun login(
        @Body request: LoginReq
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body request: RegisterReq
    ): RegisterResponse
}