package com.chatychaty.app.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogChatBinding
import com.chatychaty.app.message.MessageListViewModel
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatItemDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogChatBinding

    private val args by navArgs<ChatItemDialogFragmentArgs>()

    private val viewModel by viewModel<MessageListViewModel> { parametersOf(args.chatId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentDialogChatBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
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
                        binding.tvViewProfile.text = "View ${chat.name}'s profile"

                        val archiveDrawable = ResourcesCompat.getDrawable(
                            resources,
                            if (chat.isArchived) R.drawable.ic_round_unarchive_24 else R.drawable.ic_round_archive_24,
                            null,
                        )
                        binding.tvArchiveChat.text = getString(if (chat.isArchived) R.string.unarchive_chat else R.string.archive_chat)
                        binding.tvArchiveChat.setCompoundDrawablesWithIntrinsicBounds(archiveDrawable, null, null, null)

                        val pinDrawable = ResourcesCompat.getDrawable(
                            resources,
                            if (chat.isPinned) R.drawable.ic_round_pin_off_24 else R.drawable.ic_round_pin_24,
                            null,
                        )
                        binding.tvPin.setCompoundDrawablesWithIntrinsicBounds(pinDrawable, null, null, null)
                        binding.tvPin.text = getString(if (chat.isPinned) R.string.unpin else R.string.pin)

                        val muteDrawable = ResourcesCompat.getDrawable(
                            resources,
                            if (chat.isMuted) R.drawable.ic_round_volume_up_24 else R.drawable.ic_round_volume_off_24,
                            null,
                        )
                        binding.tvMute.setCompoundDrawablesWithIntrinsicBounds(muteDrawable, null, null, null)
                        binding.tvMute.text = getString(if (chat.isMuted) R.string.unmute else R.string.mute)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        val parentView = requireParentFragment().requireView()
        val parentFab = parentView.findViewById<FloatingActionButton>(R.id.fab)

        binding.tvArchiveChat.setOnClickListener {
            dismiss()
            if ((viewModel.chat.value as UiState.Success).value.isArchived) {
                viewModel.unarchiveChat()
                parentView.snackbar(getString(R.string.chat_is_unarchived))
            } else {
                viewModel.archiveChat()
                parentView.snackbar(getString(R.string.chat_is_archived), anchorView = parentFab)
            }
        }

        binding.tvChangeColor.setOnClickListener {
            dismiss()
            parentView.snackbar("TODO", anchorView = parentFab)
        }

        binding.tvDeleteChat.setOnClickListener {
            dismiss()
            parentView.snackbar("TODO", anchorView = parentFab)
        }

        binding.tvPin.setOnClickListener {
            dismiss()
            if ((viewModel.chat.value as UiState.Success).value.isPinned) {
                viewModel.unpinChat()
                parentView.snackbar(getString(R.string.chat_is_unpinned), anchorView = parentFab)
            } else {
                viewModel.pinChat()
                parentView.snackbar(getString(R.string.chat_is_pinned), anchorView = parentFab)
            }
        }

        binding.tvViewProfile.setOnClickListener {
            dismiss()
            findNavController().navigate(ChatItemDialogFragmentDirections.actionChatDialogFragmentToProfileFragment(args.chatId))
        }

        binding.tvMute.setOnClickListener {
            dismiss()
            if ((viewModel.chat.value as UiState.Success).value.isMuted) {
                viewModel.unmuteChat()
                parentView.snackbar(getString(R.string.chat_is_unmuted), anchorView = parentFab)
            } else {
                viewModel.muteChat()
                parentView.snackbar(getString(R.string.chat_is_muted), anchorView = parentFab)
            }
        }

    }

}