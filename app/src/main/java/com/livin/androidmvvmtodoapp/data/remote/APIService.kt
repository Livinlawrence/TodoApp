package com.livin.androidmvvmtodoapp.data.remote

import com.livin.androidmvvmtodoapp.data.remote.requests.LoginRequestModel
import com.livin.androidmvvmtodoapp.data.remote.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {

    @POST("login")
    suspend fun login(
        @Body loginRequestModel: LoginRequestModel
    ): LoginResponse
}