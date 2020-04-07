package com.apps.chatychaty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.ListItemChatBinding
import com.apps.chatychaty.model.Chat
import com.bumptech.glide.Glide

internal class ListRVAdapter(
    private val navigateToChat: NavigateToChat
) :
    ListAdapter<Chat, ChatItemViewHolder>(ChatItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder.create(parent, navigateToChat)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
        holder.chat = chat
    }
}


internal class ChatItemViewHolder(
    private val binding: ListItemChatBinding,
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
            navigateToChat: NavigateToChat
        ): ChatItemViewHolder {

            return ChatItemViewHolder(
                ListItemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), navigateToChat
            )
        }
    }

    fun bind(chat: Chat) {

        binding.name.text = chat.user.name

        binding.body.text = navigateToChat.getLastMessage(chat.chatId)

        Glide.with(itemView)
            .load(chat.user.imgUrl)
            .placeholder(itemView.context.getDrawable(R.drawable.ic_person_24dp))
            .circleCrop()
            .into(binding.img)
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

    fun getLastMessage(chatId: Int): String
}