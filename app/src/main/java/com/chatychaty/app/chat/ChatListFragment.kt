package com.chatychaty.app.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentListChatBinding
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatListFragment : Fragment() {

    private lateinit var binding: FragmentListChatBinding

    private val viewModel by viewModel<ChatListViewModel>()

    private val adapter by lazy { ChatListAdapter(chatItemListener) }

    private val chatItemListener by lazy {
        object : ChatListAdapter.ChatItemListener {
            override fun navigateToChat(chat: Chat) =
                findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToMessageListFragment(chat.chatId))

            override fun navigateToProfile(chat: Chat) =
                findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToProfileFragment(chat.chatId))

            override fun navigateToChatDialogFragment(chat: Chat) =
                findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToChatDialogFragment(chat.chatId))
        }
    }

    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListChatBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        setupListeners()
        setupRV()
        collectState()

        return binding.root
    }

    private fun setupListeners() {
//
//        binding.ivImage.setOnClickListener {
//            findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToUserFragment())
//        }
        binding.fab.setOnClickListener {
            findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToSearchFragment())
        }
        binding.bab.setNavigationOnClickListener {
            findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToListDialogFragment())
        }

        binding.bab.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.search -> {
                    if (binding.tilSearch.visibility == View.VISIBLE)
                        disableSearch()
                    else
                        enableSearch()
                    true
                }
                R.id.archived_chats -> {
                    findNavController().navigate(ChatListFragmentDirections.actionChatListFragmentToChatListArchiveFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRV() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun collectState() {
        viewModel.chatMessages
            .onEach { state ->
                when (state) {
                    is UiState.Empty -> {
                        adapter.submitList(emptyList())
                    }
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


    private fun enableSearch() {
        binding.tilSearch.visibility = View.GONE
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.tilSearch.visibility = View.VISIBLE
        binding.etSearch.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        viewModel.filteredChatMessages
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)

        viewModel.searchTerm
            .onEach { viewModel.filterChats() }
            .launchIn(lifecycleScope)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                disableSearch()
                if (isEnabled) isEnabled = false
            }
    }

    private fun disableSearch() {
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.tilSearch.visibility = View.GONE
        binding.etSearch.setText("")
    }

}
