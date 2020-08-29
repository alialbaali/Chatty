package com.chatychaty.app.chat

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatychaty.app.R
import com.chatychaty.app.databinding.ItemReceiverMessageBinding
import com.chatychaty.app.databinding.ItemSenderMessageBinding
import com.chatychaty.domain.model.Message

private const val VIEW_TYPE_Sender_MESSAGE = 1
private const val VIEW_TYPE_RECEIVER_MESSAGE = 2

// Chat RV Adapter
class MessageRVAdapter(private val senderUsername: String, private val listener: MessageItemListener) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_Sender_MESSAGE) {
            SenderMessageItemViewHolder.create(parent, listener)
        } else {
            ReceiverMessageItemViewHolder.create(parent, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_Sender_MESSAGE) {
            holder as SenderMessageItemViewHolder

            val message = getItem(position)
            holder.message = message
            holder.bind(message)

        } else {

            holder as ReceiverMessageItemViewHolder
            val message = getItem(position)
            holder.message = message
            holder.bind(message)

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
    class SenderMessageItemViewHolder(private val binding: ItemSenderMessageBinding, private val listener: MessageItemListener) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var message: Message


        init {
            binding.root.setOnTouchListener { v, event ->

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.performClick()
                        v.performHapticFeedback(250)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        listener.onTouch(message)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }


        companion object {
            fun create(parent: ViewGroup, listener: MessageItemListener): SenderMessageItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemSenderMessageBinding.inflate(inflater, parent, false)
                return SenderMessageItemViewHolder(binding, listener)
            }
        }

        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
//            binding.time.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date()).toString()

//            binding.root.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.rv_sender_animation)
//            TODO("Add Time in hours and mins")
        }

    }

    // Receiver Message ViewHolder
    class ReceiverMessageItemViewHolder(private val binding: ItemReceiverMessageBinding, private val listener: MessageItemListener) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var message: Message

        init {
            binding.root.setOnTouchListener { v, event ->

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.performClick()
                        v.performHapticFeedback(250)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        listener.onTouch(message)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }


        companion object {
            fun create(parent: ViewGroup, listener: MessageItemListener): ReceiverMessageItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemReceiverMessageBinding.inflate(inflater, parent, false)
                return ReceiverMessageItemViewHolder(binding, listener)
            }
        }

        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
//            binding.root.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.rv_receiver_animation)

//            binding.time.text = Calendar.getInstance().time.toString()
//            TODO("Add Time in hours and mins")
        }

    }

    private class MessageItemDiffCallback() : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem.messageId == newItem.messageId

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem == newItem

    }
}


interface MessageItemListener {

    fun onTouch(message: Message)

}