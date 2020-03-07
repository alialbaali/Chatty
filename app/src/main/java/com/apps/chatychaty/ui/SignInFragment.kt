package com.apps.chatychaty.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.databinding.FragmentSignInBinding
import com.apps.chatychaty.viewModel.SignSharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<SignSharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.tvSignUp.let { tvSignUp ->
            tvSignUp.setOnClickListener {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }

        binding.btnSignIn.let { btnSignIn ->
            btnSignIn.setOnClickListener {
                this.findNavController()
                    .navigate(SignInFragmentDirections.actionGlobalChatFragment())
            }
        }


        return binding.root
    }


}
