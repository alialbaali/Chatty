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
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.FragmentSignInBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.Sign
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment(), Sign, Error {
    private lateinit var binding: FragmentSignInBinding

    private val viewModel by viewModels<SignSharedViewModel> {
        SignSharedViewModelFactory(Repos.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        enterTransition =  MaterialFadeThrough.create(requireContext()).apply {
            duration = 500
        }

        exitTransition =  MaterialFadeThrough.create(requireContext()).apply {
            duration = 500
        }
        // Binding
        binding.let {

            it.lifecycleOwner = this

            it.viewModel = viewModel

        }


        binding.tvSignUp.setOnClickListener {
            this.findNavController()
                .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }



        binding.btnSignIn.setOnClickListener {
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
            }
        }

        // ViewModel
        viewModel.let { viewModel ->

            viewModel.error = this
            viewModel.sign = this

        }

        return binding.root
    }

    override fun snackbar(value: String) {
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }

    override fun putPreferences(token: String, name: String, username: String, imgUrl: String) {
        this.findNavController().navigate(SignInFragmentDirections.actionGlobalListFragment())

        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
            this.putString("token", token)
            this.putString("name", name)
            this.putString("username", username)
            this.putString("img_url", imgUrl)
            apply()
        }
    }

//    override fun putPreferences(username: String, token: String?) {
//
//        this.findNavController().navigate(SignInFragmentDirections.actionGlobalListFragment())
//
//        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
//            this.putString("username", username)
//            this.putString("token", token)
//        }
//
//    }
}
