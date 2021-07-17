package com.chatychaty.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentSignInBinding
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel by sharedViewModel<SignSharedViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        setupListeners()
        collectState()

        return binding.root
    }

    private fun setupListeners() {

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.btnSignIn.setOnClickListener {

            val username = viewModel.username.value
            val password = viewModel.password.value

            if (username.isBlank()) binding.tilUsername.error = resources.getString(R.string.username_error)

            if (password.isBlank()) binding.tilPassword.error = resources.getString(R.string.password_error_empty)

            if (username.isNotBlank() and password.isNotBlank()) {
                viewModel.signIn()
                findNavController().navigate(SignInFragmentDirections.actionGlobalProgressDialogFragment())
            }
        }
    }

    private fun collectState() {
        viewModel.state
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {
                        findNavController().navigateUp()
                        binding.root.snackbar(state.exception.message.toString())
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

}