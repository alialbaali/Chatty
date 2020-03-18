package com.apps.chatychaty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.apps.chatychaty.databinding.ListItemSearchUserBinding
import com.apps.chatychaty.model.User

internal class SearchAdapter :
    ListAdapter<User, SearchAdapter.SearchUserItemViewHolder>(SearchUserItemDiffCallback()) {

    private lateinit var navigateToChat: NavigateToChat

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserItemViewHolder {
        return SearchUserItemViewHolder.create(parent, navigateToChat)
    }

    override fun onBindViewHolder(holder: SearchUserItemViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.user = user
        TODO("Check what data to pass")
    }

    class SearchUserItemViewHolder(
        private val binding: ListItemSearchUserBinding,
        private val navigateToChat: NavigateToChat
    ) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var user: User

        init {
            binding.root.setOnClickListener {
                navigateToChat.navigate(user)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                navigateToChat: NavigateToChat
            ): SearchUserItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemSearchUserBinding.inflate(inflater, parent, false)
                return SearchUserItemViewHolder(binding, navigateToChat)
            }
        }

        fun bind(user: User) {
            binding.name.text = user.name
            binding.username.text = user.username
            TODO("Bind img to user's img")
        }

    }

}

private class SearchUserItemDiffCallback() : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

interface NavigateToChat {
    fun navigate(user: User)
}