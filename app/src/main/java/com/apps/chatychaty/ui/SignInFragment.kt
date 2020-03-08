package com.apps.chatychaty.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.databinding.FragmentSignInBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.LogIn
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment(), LogIn {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<SignSharedViewModel> {
        SignSharedViewModelFactory(Repos.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        // Binding
        binding.let {

            it.lifecycleOwner = this
            it.viewModel = viewModel

        }

        // Sign Up TextView
        binding.tvSignUp.let { tvSignUp ->

            tvSignUp.setOnClickListener {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }

        }

        // Sign In Button
        binding.btnSignIn.let { btnSignIn ->

            btnSignIn.setOnClickListener {
                viewModel.logIn()
            }

        }

        // ViewModel
        viewModel.let { viewModel ->

            viewModel.logIn = this

            viewModel.authorized.observe(viewLifecycleOwner, Observer {

                if (it) {
                    this.findNavController()
                        .navigate(SignInFragmentDirections.actionGlobalChatFragment())
                }
            })

        }


        return binding.root
    }

    override fun showSnackbar(value: String) {
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG).show()
    }


}
