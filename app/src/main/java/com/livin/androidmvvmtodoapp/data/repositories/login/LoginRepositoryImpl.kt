package com.livin.androidmvvmtodoapp.data.repositories.login

import com.livin.androidmvvmtodoapp.data.remote.APIService
import com.livin.androidmvvmtodoapp.data.remote.requests.LoginRequestModel
import com.livin.androidmvvmtodoapp.data.remote.responses.LoginResponse
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val dispatchers: DispatcherProvider,
) : LoginRepository {

    override suspend fun login(loginRequestModel: LoginRequestModel): Flow<LoginResponse> {
        return flow {
            emit(apiService.login(loginRequestModel))
        }.flowOn(dispatchers.io)
    }
}