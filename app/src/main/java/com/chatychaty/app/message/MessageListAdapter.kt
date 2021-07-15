package com.chatychaty.app.message

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatychaty.app.databinding.ItemReceiverMessageBinding
import com.chatychaty.app.databinding.ItemSenderMessageBinding
import com.chatychaty.app.util.getDisplayTime
import com.chatychaty.app.util.statusDrawable
import com.chatychaty.domain.model.Message

private const val SENDER_VIEW_TYPE = 1
private const val RECEIVER_VIEW_TYPE = 2

class MessageListAdapter(
    private val receiverUsername: String,
    private val listener: MessageItemListener
) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENDER_VIEW_TYPE)
            SenderMessageItemViewHolder.create(parent, listener)
        else
            ReceiverMessageItemViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == SENDER_VIEW_TYPE) {
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
        return if (this.currentList[position].username == receiverUsername)
            RECEIVER_VIEW_TYPE
        else
            SENDER_VIEW_TYPE
    }

    class SenderMessageItemViewHolder(
        private val binding: ItemSenderMessageBinding,
        private val listener: MessageItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

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
            binding.tvTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, message.statusDrawable, 0)
            val is24HourFormat = DateFormat.is24HourFormat(binding.root.context)
            binding.tvTime.text = message.getDisplayTime(is24HourFormat)
        }

    }

    class ReceiverMessageItemViewHolder(
        private val binding: ItemReceiverMessageBinding,
        private val listener: MessageItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

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
            val is24HourFormat = DateFormat.is24HourFormat(binding.root.context).also(::println)
            binding.tvTime.text = message.getDisplayTime(is24HourFormat)
        }

    }

    private class MessageItemDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem == newItem
    }

    fun interface MessageItemListener {
        fun onTouch(message: Message)
    }
}