package com.chatychaty.app.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chatychaty.app.databinding.FragmentProfileBinding
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModel<ProfileViewModel> { parametersOf(args.chatId) }

    private val args by navArgs<ProfileFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        collectState()
        setupListeners()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun collectState() {
        viewModel.chat
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {

                    }
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        val chat = state.value

                        binding.tvName.text = chat.name
                        binding.tvUsername.text = "@${chat.username}"
                        binding.imageUrl = chat.imageUrl
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        binding.tb.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvBlockUser.setOnClickListener {
            binding.root.snackbar("TODO")
        }
        binding.tvChangeColor.setOnClickListener {
            binding.root.snackbar("TODO")
        }
        binding.tvDeleteChat.setOnClickListener {
            binding.root.snackbar("TODO")
        }
    }

}