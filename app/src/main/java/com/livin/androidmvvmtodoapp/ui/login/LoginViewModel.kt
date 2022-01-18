package com.livin.androidmvvmtodoapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.data.remote.requests.LoginRequestModel
import com.livin.androidmvvmtodoapp.data.repositories.login.LoginRepository
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import com.livin.androidmvvmtodoapp.utils.LoginUtil.isValidEmail
import com.livin.androidmvvmtodoapp.utils.LoginUtil.isValidForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val dispatchers: DispatcherProvider,
    private val loginRepository: LoginRepository
) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val _loginUiState = MutableSharedFlow<LoginUiState>()
    val loginUiState: SharedFlow<LoginUiState> = _loginUiState.asSharedFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch(dispatchers.main) {
            if (!isValidForm(username, password)) {
                _loginUiState.emit(
                    LoginUiState.Error(
                        context.getString(R.string.fields_must_not_empty)
                    )
                )
                return@launch
            }

            if (!isValidEmail(username)) {
                _loginUiState.emit(
                    LoginUiState.Error(
                        context.getString(R.string.invalid_email_address)
                    )
                )
                return@launch
            }
            loginRepository.login(LoginRequestModel(username, password)).onStart {
                _loginUiState.emit(LoginUiState.Loading)
            }.catch { throwable ->
                when (throwable) {
                    is HttpException -> {
                        if (throwable.code() == 400) {
                            _loginUiState.emit(
                                LoginUiState.Error(
                                    context.getString(R.string.invalid_email_or_password)
                                )
                            )
                        }
                    }
                    else -> {
                        _loginUiState.emit(
                            LoginUiState.Error(
                                throwable.message
                                    ?: context.getString(R.string.something_went_wrong)
                            )
                        )
                    }
                }

            }.collect { loginResponse ->
                if (loginResponse.token?.isNotEmpty() == true) {
                    _loginUiState.emit(LoginUiState.Success)
                } else {
                    _loginUiState.emit(
                        LoginUiState.Error(
                            loginResponse.error
                                ?: context.getString(R.string.invalid_email_or_password)
                        )
                    )
                }
            }
        }
    }


    sealed class LoginUiState {
        object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        object Loading : LoginUiState()
    }
}