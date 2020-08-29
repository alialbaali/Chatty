package com.chatychaty.app.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatychaty.app.databinding.ItemChatPinnedBinding
import com.chatychaty.domain.model.Chat

class PinnedChatRVAdapter() : ListAdapter<Chat, PinnedChatRVAdapter.PinnedChatItemViewHolder>(PinnedChatItemDiffCallback()) {

    class PinnedChatItemViewHolder(private val binding: ItemChatPinnedBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var chat: Chat

        companion object {

            fun create(parent: ViewGroup): PinnedChatItemViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = ItemChatPinnedBinding.inflate(layoutInflater, parent, false)

                return PinnedChatItemViewHolder(binding)

            }

        }

        fun bind(chat: Chat) {

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinnedChatItemViewHolder = PinnedChatItemViewHolder.create(parent)

    override fun onBindViewHolder(holder: PinnedChatItemViewHolder, position: Int) {
        val chat = getItem(position)
        holder.chat = chat
        holder.bind(chat)
    }


    private class PinnedChatItemDiffCallback : DiffUtil.ItemCallback<Chat>() {

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean = oldItem.chatId == newItem.chatId

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean = oldItem == newItem

    }

}