package com.chatychaty.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentSignUpBinding
import com.chatychaty.app.util.snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by viewModel<SignSharedViewModel>()

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@SignUpFragment
            viewModel = this@SignUpFragment.viewModel
        }

        binding.tvSignIn.setOnClickListener {
            this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

        binding.btnSignUp.setOnClickListener {

            progressDialog = ProgressDialog().also {
                it.show(parentFragmentManager, null)
            }

            val name = binding.etName.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isBlank()) binding.tilName.error = resources.getString(R.string.name_error)

            if (username.isBlank()) binding.tilUsername.error = resources.getString(R.string.username_error)

            if (password.isBlank()) binding.tilPassword.error = resources.getString(R.string.password_error)

            if (name.isNotBlank() && username.isNotBlank() && password.isNotBlank()) viewModel.signUp()

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
                findNavController().navigate(SignUpFragmentDirections.actionGlobalListFragment())
                viewModel.onNavigate(false)
            }
        })

        return binding.root
    }
}