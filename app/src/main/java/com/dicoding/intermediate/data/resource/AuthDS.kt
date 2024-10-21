package com.dicoding.intermediate.data.resource

import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.network.request.LoginReq
import com.dicoding.intermediate.data.network.request.RegisterReq
import com.dicoding.intermediate.data.network.services.AuthServ
import com.dicoding.intermediate.di.networkModule
import com.dicoding.intermediate.di.preferModule
import com.dicoding.intermediate.utils.PreferManager
import com.dicoding.intermediate.utils.helper.createResponse
import com.dicoding.intermediate.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthDS(
    private val service: AuthServ, private val pref: PreferManager
) {

    suspend fun login(
        request: LoginReq
    ): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                wrapEspressoIdlingResource{
                    val response = service.login(request)
                    if (response.error) {
                        emit(ApiResponse.Error(response.message))
                        return@flow
                    }

                    pref.storeLoginData(response.data.token)
                    reloadModule()
                    emit(ApiResponse.Success(response.message))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

    suspend fun register(
        request: RegisterReq
    ): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.register(request)
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }
                emit(ApiResponse.Success(response.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }


    private fun reloadModule() {
        unloadKoinModules(preferModule)
        loadKoinModules(preferModule)

        unloadKoinModules(networkModule)
        loadKoinModules(networkModule)
    }
}