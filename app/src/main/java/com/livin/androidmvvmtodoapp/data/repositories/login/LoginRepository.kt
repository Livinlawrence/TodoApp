package com.livin.androidmvvmtodoapp.data.repositories.login

import com.livin.androidmvvmtodoapp.data.remote.requests.LoginRequestModel
import com.livin.androidmvvmtodoapp.data.remote.responses.LoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(loginRequestModel: LoginRequestModel): Flow<LoginResponse>
}