package com.livin.androidmvvmtodoapp.data.repositories.login

import com.livin.androidmvvmtodoapp.data.remote.requests.LoginRequestModel
import com.livin.androidmvvmtodoapp.data.remote.responses.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLoginRepo : LoginRepository {

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun login(loginRequestModel: LoginRequestModel): Flow<LoginResponse> {
        return if (shouldReturnNetworkError) {
            throw RuntimeException("No internet available")
        } else {
            flow {
                emit(LoginResponse(token = "xqwuqoi_0121"))
            }
        }
    }

}