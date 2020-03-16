package com.apps.chatychaty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apps.chatychaty.databinding.ListItemChatBinding
import com.apps.chatychaty.model.Chat

internal class ListRVAdapter : ListAdapter<Chat, ChatItemViewHolder>(ChatItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}


internal class ChatItemViewHolder(private val binding: ListItemChatBinding) :
    RecyclerView.ViewHolder(binding.root) {


    companion object {
        internal fun create(parent: ViewGroup): ChatItemViewHolder {
            return ChatItemViewHolder(
                ListItemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(item: Chat) {

    }
}

private class ChatItemDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }

}