package com.chatychaty.app.chat

import android.graphics.Typeface
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatychaty.app.R
import com.chatychaty.app.chat.ChatListAdapter.ChatItemViewHolder
import com.chatychaty.app.databinding.ItemChatBinding
import com.chatychaty.app.util.getDisplayTime
import com.chatychaty.app.util.statusDrawable
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.model.Message

class ChatListAdapter(private val chatItemListener: ChatItemListener) : ListAdapter<Pair<Chat, Message>, ChatItemViewHolder>(ChatItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder.create(parent, chatItemListener)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val pair = getItem(position)
        holder.bind(pair)
        holder.pair = pair
    }

    class ChatItemViewHolder(
        private val binding: ItemChatBinding,
        private val listener: ChatItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var pair: Pair<Chat, Message>

        init {
            binding.root.setOnClickListener {
                listener.navigateToChat(pair.first)
            }
            binding.ivProfile.setOnClickListener {
                listener.navigateToProfile(pair.first)
            }
            binding.root.setOnLongClickListener {
                listener.navigateToChatDialogFragment(pair.first)
                true
            }
        }

        companion object {

            fun create(parent: ViewGroup, listener: ChatItemListener): ChatItemViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatBinding.inflate(layoutInflater, parent, false)

                return ChatItemViewHolder(binding, listener)
            }
        }

        fun bind(pair: Pair<Chat, Message>) {
            binding.chat = pair.first
            binding.executePendingBindings()
            binding.ivPin.isVisible = pair.first.isPinned
            binding.tvBody.text = pair.second.body
            if (pair.first.username != pair.second.username && pair.second.body.isNotBlank()) {
                binding.ivStatus.setImageResource(pair.second.statusDrawable)
            }
            if (pair.second.body.isNotBlank()) {
                val is24HourFormat = DateFormat.is24HourFormat(binding.root.context)
                binding.tvTime.text = pair.second.getDisplayTime(is24HourFormat)
            }
            if (pair.second.isNew) {
                binding.ivStatus.setImageResource(R.drawable.shape_new_message)
                binding.ivStatus.updateLayoutParams {
                    val scale = binding.root.resources.displayMetrics.density
                    val size = (12 * scale + 0.5F).toInt()
                    width = size
                    height = size
                }
                binding.tvBody.setTextColor(binding.root.context.getColor(R.color.colorPrimary))
                binding.tvBody.typeface = Typeface.DEFAULT_BOLD
            }
            if (pair.second.body.isBlank()) {
                binding.tvBody.text = binding.root.context.getString(R.string.no_messages)
                binding.tvBody.setTypeface(null, Typeface.ITALIC)
            }

            binding.ivMute.isVisible = pair.first.isMuted
        }

    }

    private class ChatItemDiffCallback : DiffUtil.ItemCallback<Pair<Chat, Message>>() {
        override fun areItemsTheSame(oldItem: Pair<Chat, Message>, newItem: Pair<Chat, Message>): Boolean =
            oldItem.first.chatId == newItem.first.chatId

        override fun areContentsTheSame(oldItem: Pair<Chat, Message>, newItem: Pair<Chat, Message>): Boolean = oldItem == newItem
    }

    interface ChatItemListener {
        fun navigateToChat(chat: Chat)
        fun navigateToProfile(chat: Chat)
        fun navigateToChatDialogFragment(chat: Chat)
    }
}