package com.chatychaty.app.chat.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.chat.ChatListAdapter
import com.chatychaty.app.databinding.FragmentArchiveListChatBinding
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatListArchiveFragment : Fragment() {

    private lateinit var binding: FragmentArchiveListChatBinding

    private val viewModel by viewModel<ChatListArchiveViewModel>()

    private val adapter: ChatListAdapter by lazy { ChatListAdapter(chatItemListener) }

    private val chatItemListener by lazy {
        object : ChatListAdapter.ChatItemListener {
            override fun navigateToChat(chat: Chat) {
                findNavController().navigate(ChatListArchiveFragmentDirections.actionChatListArchiveFragmentToMessageListFragment(chat.chatId))
            }

            override fun navigateToProfile(chat: Chat) {
                findNavController().navigate(ChatListArchiveFragmentDirections.actionChatListArchiveFragmentToProfileFragment(chat.chatId))
            }

            override fun navigateToChatDialogFragment(chat: Chat) {
                findNavController().navigate(ChatListArchiveFragmentDirections.actionChatListArchiveFragmentToChatItemDialogFragment(chat.chatId))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArchiveListChatBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }

        setupRV()
        setupListeners()
        collectState()

        return binding.root
    }

    private fun collectState() {
        viewModel.archivedChats
            .onEach { state ->
                when (state) {
                    UiState.Empty -> {

                    }
                    is UiState.Failure -> {

                    }
                    UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        val archivedChats = state.value
                        if (archivedChats.isEmpty()) {
                            binding.tvPlaceholder.visibility = View.VISIBLE
                            binding.rv.visibility = View.GONE
                        } else {
                            binding.tvPlaceholder.visibility = View.GONE
                            binding.rv.visibility = View.VISIBLE
                            adapter.submitList(archivedChats)
                        }
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupRV() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupListeners() {
        binding.tb.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}