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
import com.alialbaali.chatychaty.databinding.FragmentSignUpBinding
import com.alialbaali.chatychaty.util.snackbar
import com.alialbaali.chatychaty.viewModel.SignSharedViewModel
import com.alialbaali.chatychaty.ui.SignUpFragmentDirections
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val binding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
    }

    private val viewModel by viewModel<SignSharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exitTransition = MaterialFadeThrough.create().apply {
            duration = DURATION
        }

        enterTransition = MaterialFadeThrough.create().apply {
            duration = DURATION
        }

        binding.tvSignIn.setOnClickListener {
            this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

        binding.btnSignUp.setOnClickListener {

            exitTransition = MaterialSharedAxis.create(MaterialSharedAxis.Z, true).apply { duration =
                DURATION
            }

            val name = binding.etName.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isBlank()) {
                binding.tilName.error = resources.getString(R.string.name_error)
            }
            if (username.isBlank()) {
                binding.tilUsername.error = resources.getString(R.string.username_error)
            }
            if (password.isBlank()) {
                binding.tilPassword.error = resources.getString(R.string.password_error)
            }

            if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                viewModel.signUp()

                binding.btnSignUp.showProgress {
                    this.progressColor = Color.WHITE
                    this.buttonText = "Signing Up"
                }
            }
        }

        viewModel.errors.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.any { it.contains("Username", true) }) {
                    binding.tilUsername.error = it.toString()
                    binding.btnSignUp.hideProgress("Sign Up")
                } else if (it.any { it.contains("Password", true) }) {
                    binding.tilPassword.error = it.toString()
                    binding.btnSignUp.hideProgress("Sign Up")
                } else {
                    binding.cool.snackbar(it.toString())
                    binding.btnSignUp.hideProgress("Sign Up")
                }
            }
        })

        binding.btnSignUp.attachTextChangeAnimator {
            this.fadeInMills = 250
            this.fadeOutMills = 250
        }

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it) {
                lifecycleScope.launch {
                    binding.btnSignUp.hideProgress("Signed Up")
                    delay(1000)
                    binding.btnSignUp.hideProgress("Signed up")
                    this@SignUpFragment.findNavController().navigate(SignUpFragmentDirections.actionGlobalListFragment())
                    viewModel.onNavigate(false)
                }
            }
        })

        return binding.root
    }
}