package com.chatychaty.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentSignInBinding
import com.chatychaty.app.util.snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel by viewModel<SignSharedViewModel>()

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@SignInFragment
            viewModel = this@SignInFragment.viewModel
        }


        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.btnSignIn.setOnClickListener {

            progressDialog = ProgressDialog().also {
                it.show(parentFragmentManager, null)
            }

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isBlank()) binding.tilUsername.error = resources.getString(R.string.username_error)

            if (password.isBlank()) binding.tilPassword.error = resources.getString(R.string.password_error_empty)

            if (username.isNotBlank() and password.isNotBlank()) viewModel.signIn()

        }

        viewModel.errors.observe(viewLifecycleOwner, Observer {

            progressDialog.dismiss()

            when {
                it.contains("Username", true) -> binding.tilUsername.error = it.toString()
                it.contains("Password", true) -> binding.tilPassword.error = it.toString()
                else -> binding.cool.snackbar(it.toString())
            }

        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressDialog.dismiss()
                findNavController().navigate(SignInFragmentDirections.actionGlobalListFragment())
                viewModel.onNavigate(false)
            }
        })

        return binding.root
    }
}