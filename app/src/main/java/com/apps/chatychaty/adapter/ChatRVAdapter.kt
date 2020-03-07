package com.apps.chatychaty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apps.chatychaty.databinding.MessageItemBinding
import com.apps.chatychaty.model.Message

class ChatRVAdapter() : ListAdapter<Message, MessageItemViewHolder>(MessageItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        return MessageItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class MessageItemViewHolder(private val binding: MessageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): MessageItemViewHolder {
            return MessageItemViewHolder(
                MessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }


    fun bind(item: Message) {
        binding.chatItemTvText.text = item.text
        binding.chatItemTvUsername.text = item.user
    }
}

class MessageItemDiffCallback() : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

}