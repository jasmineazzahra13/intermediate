package com.dicoding.intermediate.data.repo

import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.network.request.LoginReq
import com.dicoding.intermediate.data.network.request.RegisterReq
import com.dicoding.intermediate.data.resource.AuthDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AuthRepo (
    private val dataSource: AuthDS,
) {
    suspend fun login(
        email: String,
        password: String
    ): Flow<ApiResponse<String>> {
        val request = LoginReq(
            email = email,
            password = password
        )
        return dataSource.login(request).flowOn(Dispatchers.IO)
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<ApiResponse<String>> {
        val request = RegisterReq(
            name = name,
            email = email,
            password = password
        )
        return dataSource.register(request).flowOn(Dispatchers.IO)
    }
}