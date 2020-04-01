package com.apps.chatychaty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.adapter.ListRVAdapter
import com.apps.chatychaty.adapter.NavigateToChat
import com.apps.chatychaty.databinding.FragmentListBinding
import com.apps.chatychaty.getPref
import com.apps.chatychaty.model.Chat
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.snackbar
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.SharedViewModel
import com.apps.chatychaty.viewModel.SharedViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment(), NavigateToChat, Error {

    private lateinit var binding: FragmentListBinding

    private lateinit var adapter: ListRVAdapter

    private val args by navArgs<ListFragmentArgs>()

    private val viewModel by viewModels<SharedViewModel> {
        SharedViewModelFactory(Repos.chatRepository, Repos.messageRepository, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        adapter = ListRVAdapter(this)

        Glide.with(this)
            .load(activity?.getPref("img_url"))
            .placeholder(resources.getDrawable(R.drawable.ic_person_24dp, null))
            .circleCrop()
            .into(binding.img)

        if (args.username.isNotBlank()) {
            viewModel.insertChat(args.username)
        }

        binding.rv.let { rv ->

            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.chats.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }

        binding.img.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true).apply {
                    duration = DURATION
                }

            val name = activity?.getPref("name")!!
            val username = activity?.getPref("username")!!
            val imgUrl = activity?.getPref("img_url")
            this.findNavController().navigate(
                ListFragmentDirections.actionListFragmentToProfileFragment(
                    name,
                    username,
                    imgUrl,
                    0
                )
            )
        }

        binding.fab.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, true).apply {
                    duration = DURATION
                }

            this.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToSearchFragment())
        }

        viewModel.let { viewModel ->
            lifecycleScope.launch(Dispatchers.IO) {

                whenStarted {
                    viewModel.updateChats()
                    viewModel.updateMessages()
                }

                while (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    delay(1000)
                    viewModel.checkUpdates()
                }
            }
        }


        return binding.root
    }

    override fun navigate(chat: Chat) {
        this.findNavController()
            .navigate(
                ListFragmentDirections.actionListFragmentToChatFragment(
                    chat.chatId,
                    chat.user.name,
                    chat.user.username,
                    chat.user.imgUrl
                )
            )
    }

    override fun navigateToUser(chat: Chat) {
        this.findNavController().navigate(
            ListFragmentDirections.actionListFragmentToProfileFragment(
                chat.user.name,
                chat.user.username,
                chat.user.imgUrl,
                chat.chatId
            )
        )
    }

    override fun getLastMessage(chatId: Int): String {
        return viewModel.getLastMessage(chatId)
    }

    override fun snackbar(value: String) {
        binding.cool.snackbar(value)
    }
}
