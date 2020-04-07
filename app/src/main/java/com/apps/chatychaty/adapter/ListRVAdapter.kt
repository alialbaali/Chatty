package com.apps.chatychaty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apps.chatychaty.databinding.ListItemChatBinding
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.viewModel.SharedViewModel

internal class ListRVAdapter(
    private val viewModel: SharedViewModel,
    private val navigateToChat: NavigateToChat
) :
    ListAdapter<Chat, ChatItemViewHolder>(ChatItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder.create(parent, viewModel, navigateToChat)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
        holder.chat = chat
    }
}


internal class ChatItemViewHolder(
    private val binding: ListItemChatBinding,
    private val viewModel: SharedViewModel,
    private val navigateToChat: NavigateToChat
) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var chat: Chat

    init {
        binding.root.setOnClickListener {
            navigateToChat.navigate(chat)
        }
        binding.img.setOnClickListener {
            navigateToChat.navigateToUser(chat)
        }
    }

    companion object {

        internal fun create(
            parent: ViewGroup,
            viewModel: SharedViewModel,
            navigateToChat: NavigateToChat
        ): ChatItemViewHolder {

            return ChatItemViewHolder(
                ListItemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), viewModel, navigateToChat
            )
        }
    }

    fun bind(chat: Chat) {
        binding.chat = chat
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}

private class ChatItemDiffCallback : DiffUtil.ItemCallback<Chat>() {

    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.chatId == newItem.chatId
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }

}

interface NavigateToChat {
    fun navigate(chat: Chat)

    fun navigateToUser(chat: Chat)
}