package com.alialbaali.chatychaty.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alialbaali.chatychaty.DURATION
import com.alialbaali.chatychaty.R
import com.alialbaali.chatychaty.databinding.FragmentSignInBinding
import com.alialbaali.chatychaty.util.snackbar
import com.alialbaali.chatychaty.viewModel.SignSharedViewModel
import com.alialbaali.chatychaty.ui.SignInFragmentDirections
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    private val binding by lazy {
        FragmentSignInBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
    }

    private val viewModel by viewModel<SignSharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        enterTransition = MaterialFadeThrough.create().apply {
            duration = DURATION
        }

        exitTransition = MaterialFadeThrough.create().apply {
            duration = DURATION
        }

        binding.tvSignUp.setOnClickListener {
            this.findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.btnSignIn.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(MaterialSharedAxis.Z, true).apply { duration = DURATION }

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isBlank()) {
                binding.tilUsername.error = resources.getString(R.string.username_error)
            }
            if (password.isBlank()) {
                binding.tilPassword.error = resources.getString(R.string.password_error_empty)
            }

            if (username.isNotBlank() and password.isNotBlank()) {
                viewModel.signIn()
                binding.btnSignIn.showProgress {
                    this.progressColor = Color.WHITE
                    this.buttonText = "Signing In"
                }
            }
        }

        viewModel.errors.observe(viewLifecycleOwner, Observer {
            if (it.any { it.contains("Username", true) }) {
                binding.tilUsername.error = it.toString()
                binding.btnSignIn.hideProgress("Sign In")
            } else if (it.any { it.contains("Password", true) }) {
                binding.tilPassword.error = it.toString()
                binding.btnSignIn.hideProgress("Sign In")
            } else {
                binding.cool.snackbar(it.toString())
                binding.btnSignIn.hideProgress("Sign In")
            }
        })

        binding.btnSignIn.attachTextChangeAnimator {
            this.fadeInMills = 250
            this.fadeOutMills = 250
        }

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it) {
                lifecycleScope.launch {
                    binding.btnSignIn.hideProgress("Signed In")
                    delay(1000)
                    this@SignInFragment.findNavController().navigate(SignInFragmentDirections.actionGlobalListFragment())
                    viewModel.onNavigate(false)
                }
            }
        })

        return binding.root
    }
}