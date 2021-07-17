package com.chatychaty.app.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.chat.ChatListAdapter
import com.chatychaty.app.chat.ChatListViewModel
import com.chatychaty.app.databinding.FragmentShareBinding
import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShareFragment : Fragment() {

    private lateinit var binding: FragmentShareBinding

    private val viewModel by viewModel<ChatListViewModel>()

    private val adapter by lazy { ChatListAdapter(chatItemListener) }

    private val args by navArgs<ShareFragmentArgs>()

    private val chatItemListener by lazy {
        object : ChatListAdapter.ChatItemListener {
            override fun navigateToChat(chat: Chat) =
                findNavController().navigate(ShareFragmentDirections.actionShareFragmentToMessageListFragment(chat.chatId, args.body))

            override fun navigateToProfile(chat: Chat) {}
            override fun navigateToChatDialogFragment(chat: Chat) {}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShareBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        collectState()
        setupRV()

        return binding.root
    }

    private fun setupRV() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun collectState() {
        viewModel.chatMessages
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {
                        binding.root.snackbar("Failed to load chats")
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        if (state.value.isEmpty()) {
                            binding.tvPlaceholder.visibility = View.VISIBLE
                            binding.rv.visibility = View.GONE
                        } else {
                            binding.tvPlaceholder.visibility = View.GONE
                            binding.rv.visibility = View.VISIBLE
                            adapter.submitList(state.value)
                        }
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

}