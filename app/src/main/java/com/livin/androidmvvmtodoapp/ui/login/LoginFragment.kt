package com.livin.androidmvvmtodoapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.databinding.FragmentLoginBinding
import com.livin.androidmvvmtodoapp.utils.collectLatestLifecycleFlow
import com.livin.androidmvvmtodoapp.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            loginViewModel.login(
                binding.editTextEmailAddress.text.toString(),
                binding.editTextPassword.text.toString()
            )
            hideKeyboard()
        }
        collectLatestLifecycleFlow(
            loginViewModel.loginUiState
        ) {
            when (it) {
                is LoginViewModel.LoginUiState.Success -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.loggedin_successfully),
                        Snackbar.LENGTH_LONG
                    ).show()
                    setProgressVisibility(false)
                    findNavController().navigate(
                        R.id.action_navigation_login_to_home
                    )
                }
                is LoginViewModel.LoginUiState.Error -> {
                    showErrorMessage(it.message)
                    setProgressVisibility(false)
                }
                is LoginViewModel.LoginUiState.Loading -> {
                    setProgressVisibility(true)
                }
            }
        }
    }

    private fun setProgressVisibility(showProgress: Boolean) {
        with(binding) {
            if (showProgress) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Snackbar
            .make(
                binding.root,
                message,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}