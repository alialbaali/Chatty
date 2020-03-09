package com.apps.chatychaty.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.databinding.FragmentSignUpBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.LogIn
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment(), LogIn, Error {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by viewModels<SignSharedViewModel> {
        SignSharedViewModelFactory(Repos.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.let {

            it.lifecycleOwner = this
            it.viewModel = viewModel

        }

        binding.tvSignIn.let { tvSignIn ->
            tvSignIn.setOnClickListener {
                this.findNavController()
                    .navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }

        binding.btnSignUp.let { btnSignUp ->

            btnSignUp.setOnClickListener {
                viewModel.createAccount()
            }

        }

        viewModel.let { viewModel ->

            viewModel.error = this
            viewModel.logIn = this

        }

        return binding.root
    }

    override fun snackbar(value: String) {
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG).show()
    }

    override fun putPreferences(username: String, token: String?) {

        this.findNavController().navigate(SignUpFragmentDirections.actionGlobalChatFragment())

        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
            this.putString("username", username)
            this.putString("token", token)
        }

    }


}
