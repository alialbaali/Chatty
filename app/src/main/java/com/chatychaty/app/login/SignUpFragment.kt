package com.chatychaty.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentSignUpBinding
import com.chatychaty.app.util.ProgressDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by sharedViewModel<SignSharedViewModel>()

    private lateinit var progressDialogFragment: ProgressDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        setupListeners()
        collectState()

        return binding.root
    }

    private fun setupListeners() {
        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

        binding.btnSignUp.setOnClickListener {

            val name = viewModel.name.value
            val username = viewModel.name.value
            val password = viewModel.name.value

            if (name.isBlank()) binding.tilName.error = resources.getString(R.string.name_error)

            if (username.isBlank()) binding.tilUsername.error = resources.getString(R.string.username_error)

            if (password.isBlank()) binding.tilPassword.error = resources.getString(R.string.password_error)

            if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank())
                viewModel.signUp()

        }
    }

    private fun collectState() {
        viewModel.state
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {
                        progressDialogFragment.dismiss()
                        binding.root.snackbar(state.exception.message.toString())
                    }
                    is UiState.Loading -> {
                        progressDialogFragment = ProgressDialogFragment()
                        progressDialogFragment.show(parentFragmentManager, null)
                    }
                    is UiState.Success -> {
                        progressDialogFragment.dismiss()
                    }
                    is UiState.Empty -> {

                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}