package com.livin.androidmvvmtodoapp.ui.login

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.livin.androidmvvmtodoapp.TestDispatchers
import com.livin.androidmvvmtodoapp.data.repositories.login.FakeLoginRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var fakeRepo: FakeLoginRepo

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        fakeRepo = FakeLoginRepo()
        viewModel =
            LoginViewModel(ApplicationProvider.getApplicationContext(), testDispatchers, fakeRepo)
    }

    @Test
    fun empty_fields_should_return_error_state() = runBlocking {
        viewModel.loginUiState.test {
            viewModel.login("", "")
            val emission = awaitItem()
            assertThat(emission).isEqualTo(LoginViewModel.LoginUiState.Error("Email and Password must not be empty"))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun invalid_email_should_return_error_state() = runBlocking {
        viewModel.loginUiState.test {
            viewModel.login("demo@test", "123456")
            val emission = awaitItem()
            assertThat(emission).isEqualTo(LoginViewModel.LoginUiState.Error("Invalid email address"))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun valid_email_password_should_return_success_state() = runBlocking {
        fakeRepo.setShouldReturnNetworkError(false)
        viewModel.loginUiState.test {
            viewModel.login("eve.holt@reqres.in", "cityslicka")
            val loadingEmission = awaitItem()
            assertThat(loadingEmission).isEqualTo(LoginViewModel.LoginUiState.Loading)
            val successEmission = awaitItem()
            assertThat(successEmission).isEqualTo(LoginViewModel.LoginUiState.Success)
            cancelAndConsumeRemainingEvents()
        }
    }
}