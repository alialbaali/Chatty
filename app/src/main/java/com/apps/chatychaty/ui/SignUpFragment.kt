package com.apps.chatychaty.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.databinding.FragmentSignUpBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

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

        binding.btnSignUp.let { btnSignUp ->
            btnSignUp.setOnClickListener {
                this.findNavController()
                    .navigate(SignUpFragmentDirections.actionGlobalChatFragment())
            }
        }

        binding.tvSignIn.let { tvSignIn ->
            tvSignIn.setOnClickListener {
                this.findNavController()
                    .navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }

        return binding.root
    }


}
