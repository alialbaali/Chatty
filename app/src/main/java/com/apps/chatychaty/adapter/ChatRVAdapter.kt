package com.apps.chatychaty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.ListItemReceiverMessageBinding
import com.apps.chatychaty.databinding.ListItemSenderMessageBinding
import com.apps.chatychaty.model.Message
import timber.log.Timber

private const val VIEW_TYPE_Sender_MESSAGE = 1
private const val VIEW_TYPE_RECEIVER_MESSAGE = 2

// Chat RV Adapter
 class ChatRVAdapter(private val senderUsername: String) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_Sender_MESSAGE) {
            SenderMessageItemViewHolder.create(parent)
        } else {
            ReceiverMessageItemViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_Sender_MESSAGE) {
            holder as SenderMessageItemViewHolder
            holder.bind(getItem(position))
        } else {
            holder as ReceiverMessageItemViewHolder
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.currentList[position].username == senderUsername) {
            VIEW_TYPE_Sender_MESSAGE
        } else {
            VIEW_TYPE_RECEIVER_MESSAGE
        }
    }

    // Sender Message ViewHolder
    class SenderMessageItemViewHolder(private val binding: ListItemSenderMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): SenderMessageItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemSenderMessageBinding.inflate(inflater, parent, false)
                return SenderMessageItemViewHolder(binding)
            }
        }

        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
//            binding.time.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date()).toString()

//            TODO("Add Time in hours and mins")
        }

    }

    // Receiver Message ViewHolder
    class ReceiverMessageItemViewHolder(private val binding: ListItemReceiverMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): ReceiverMessageItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemReceiverMessageBinding.inflate(inflater, parent, false)
                return ReceiverMessageItemViewHolder(binding)
            }
        }

        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
//            binding.time.text = Calendar.getInstance().time.toString()
//            TODO("Add Time in hours and mins")
        }

    }
}

private class MessageItemDiffCallback() : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.messageId == newItem.messageId
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}