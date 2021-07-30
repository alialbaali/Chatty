package com.chatychaty.app.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogListMessageBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class MessageListDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogListMessageBinding

    private val viewModel by viewModel<MessageListViewModel> { parametersOf(args.chatId) }

    private val args by navArgs<MessageListDialogFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDialogListMessageBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }

        collectState()
        setupListeners()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun collectState() {
        viewModel.chat
            .onEach {
                when (it) {
                    is UiState.Failure -> {

                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        binding.tvViewProfile.text = "View ${it.value.name}'s profile"
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        val anchorView = requireParentFragment()
            .requireView()
            .findViewById<LinearLayout>(R.id.ll)

        binding.tvViewProfile.setOnClickListener {
            dismiss()
            findNavController().navigate(MessageListDialogFragmentDirections.actionMessageListDialogFragmentToProfileFragment(args.chatId))
        }

        binding.tvChangeColor.setOnClickListener {
            dismiss()
            requireParentFragment()
                .requireView()
                .snackbar("TODO", anchorView = anchorView)
        }

        binding.tvSearchMessages.setOnClickListener {
            dismiss()
            args.listener.enableSearch()
        }

        binding.tvDeleteChat.setOnClickListener {
            dismiss()
            requireParentFragment()
                .requireView()
                .snackbar("TODO", anchorView = anchorView)
        }

    }

    fun interface SearchMessagesListener : Serializable {
        fun enableSearch()
    }

}