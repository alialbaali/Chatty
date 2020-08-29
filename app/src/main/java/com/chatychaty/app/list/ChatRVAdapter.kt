package com.chatychaty.app.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatychaty.app.R
import com.chatychaty.app.SharedViewModel
import com.chatychaty.app.databinding.ItemChatBinding
import com.chatychaty.domain.model.Chat

class ListRVAdapter(private val viewModel: SharedViewModel, private val chatItemListener: ChatItemListener) :
    ListAdapter<Chat, ListRVAdapter.ChatItemViewHolder>(ChatItemDiffCallback()), ChatItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder.create(parent, viewModel, chatItemListener)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
        holder.chat = chat
    }

    override fun onMoveViewHolder(fromViewHolder: ChatItemViewHolder, toViewHolder: ChatItemViewHolder) {
//        val fromChat = currentList.find { it.chatPosition == fromViewHolder.adapterPosition }!!
//        val toChat = currentList.find { it.chatPosition == toViewHolder.adapterPosition }!!

        fromViewHolder.move(fromViewHolder, toViewHolder)

        notifyItemMoved(fromViewHolder.adapterPosition, toViewHolder.adapterPosition)
    }

    override fun onSwipeViewHolder(viewHolder: ChatItemViewHolder) {
        viewHolder.swipe(viewHolder)
    }

    override fun onClearViewHolder(viewHolder: ChatItemViewHolder) {
        viewHolder.clear(viewHolder)
    }

    @SuppressLint("RestrictedApi")
    class ChatItemViewHolder(
        private val binding: ItemChatBinding,
        private val viewModel: SharedViewModel,
        private val listener: ChatItemListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var chat: Chat

        init {
            binding.root.setOnClickListener {
                listener.navigateToChat(chat)
            }
//        binding.img.setOnClickListener {
//            listener.navigateToProfile(chat)
//        }
            binding.root.setOnLongClickListener {
                listener.navigateToChatDialogFragment(chat)
                true
            }
        }

        companion object {

            fun create(parent: ViewGroup, viewModel: SharedViewModel, listener: ChatItemListener): ChatItemViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = ItemChatBinding.inflate(layoutInflater, parent, false)

                return ChatItemViewHolder(
                    binding, viewModel, listener
                )
            }
        }

        fun bind(chat: Chat) {
            binding.chat = chat
            binding.viewModel = viewModel
            binding.executePendingBindings()
//            binding.root.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.list_item_chat_animation)
        }

        fun move(fromViewHolder: ChatItemViewHolder, toViewHolder: ChatItemViewHolder) {

        }

        fun swipe(viewHolder: ChatItemViewHolder) {

        }

        fun clear(viewHolder: ChatItemViewHolder) {

        }

    }

    private class ChatItemDiffCallback : DiffUtil.ItemCallback<Chat>() {

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean = oldItem.chatId == newItem.chatId

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean = oldItem == newItem

    }

}


interface ChatItemListener {
    fun navigateToChat(chat: Chat)

    fun navigateToProfile(chat: Chat)

    fun navigateToChatDialogFragment(chat: Chat)
}