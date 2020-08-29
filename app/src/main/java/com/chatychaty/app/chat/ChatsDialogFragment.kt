package com.chatychaty.app.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.SharedViewModel
import com.chatychaty.app.databinding.FragmentDialogChatsBinding
import com.chatychaty.app.list.ChatItemListener
import com.chatychaty.app.list.ListRVAdapter
import com.chatychaty.domain.model.Chat
import org.koin.android.viewmodel.ext.android.viewModel

class ChatsDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogChatsBinding

    private val viewModel by viewModel<SharedViewModel>()

    private val rvAdapter by lazy { ListRVAdapter(viewModel, chatItemListener) }

    private val rvLayoutManger by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
            stackFromEnd = true
        }
    }

    private val chatItemListener by lazy {
        object : ChatItemListener {
            override fun navigateToChat(chat: Chat) {
                dismiss()
                findNavController().navigate(ChatsDialogFragmentDirections.actionChatsDialogFragmentToChatFragment(chat.chatId))
            }

            override fun navigateToProfile(chat: Chat) {
                dismiss()
                findNavController().navigate(ChatsDialogFragmentDirections.actionChatsDialogFragmentToChatFragment(chat.chatId))
            }

            override fun navigateToChatDialogFragment(chat: Chat) {
                dismiss()
                findNavController().navigate(ChatsDialogFragmentDirections.actionChatsDialogFragmentToChatFragment(chat.chatId))
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentDialogChatsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ChatsDialogFragment
        }

        with(binding.rv) {

            layoutManager = rvLayoutManger
            adapter = rvAdapter

            viewModel.chats.observe(viewLifecycleOwner, Observer { chats ->
                rvAdapter.submitList(chats)
            })

        }




        return binding.root
    }

}