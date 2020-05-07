package com.alialbaali.chatychaty.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alialbaali.model.Chat
import com.alialbaali.repository.IMG_URL
import com.alialbaali.repository.NAME
import com.alialbaali.repository.USERNAME
import com.alialbaali.chatychaty.DURATION
import com.alialbaali.chatychaty.adapter.ListRVAdapter
import com.alialbaali.chatychaty.adapter.NavigateToChat
import com.alialbaali.chatychaty.databinding.FragmentListBinding
import com.alialbaali.chatychaty.util.getPref
import com.alialbaali.chatychaty.util.snackbar
import com.alialbaali.chatychaty.viewModel.SharedViewModel
import com.alialbaali.chatychaty.ui.ListFragmentArgs
import com.alialbaali.chatychaty.ui.ListFragmentDirections
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ListFragment : Fragment(), NavigateToChat {

    private val binding by lazy {
        FragmentListBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
        }
    }

    private val rvAdapter by lazy { ListRVAdapter(viewModel, this) }

    private val args by navArgs<ListFragmentArgs>()

    private val viewModel by viewModel<SharedViewModel>()

    private val sharedPreferences by inject<SharedPreferences>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.imgUrl = requireActivity().getPref(IMG_URL)

        if (args.username.isNotBlank()) {
            viewModel.insertChat(args.username)
        }

        binding.rv.let { rv ->

            rv.adapter = rvAdapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.chats.observe(viewLifecycleOwner, Observer {
                it?.let {
                    rvAdapter.submitList(it)
                }
            })
        }

        binding.img.setOnClickListener {

            enterTransition =
                MaterialSharedAxis.create(MaterialSharedAxis.Y, false).apply {
                    duration = DURATION
                }

            exitTransition =
                MaterialSharedAxis.create(MaterialSharedAxis.Y, true).apply {
                    duration = DURATION
                }


            val name = requireContext().getPref(NAME)!!
            val username = requireContext().getPref(USERNAME)!!
            val imgUrl = requireContext().getPref(IMG_URL)
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

            enterTransition =
                MaterialSharedAxis.create(MaterialSharedAxis.Y, false).apply {
                    duration = DURATION
                }

            exitTransition =
                MaterialSharedAxis.create(MaterialSharedAxis.Y, true).apply {
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

                while (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    delay(1000)
                    viewModel.checkUpdates()
                }
            }
        }

        viewModel.errors.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.cool.snackbar(it)
            }
        })


        return binding.root
    }

    override fun navigate(chat: Chat) {

        enterTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.X, false).apply {
                duration = DURATION
            }

        exitTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.X, true).apply {
                duration = DURATION
            }

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

        enterTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.Y, false).apply {
                duration = DURATION
            }

        exitTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.Y, true).apply {
                duration = DURATION
            }

        this.findNavController().navigate(
            ListFragmentDirections.actionListFragmentToProfileFragment(
                chat.user.name,
                chat.user.username,
                chat.user.imgUrl,
                chat.chatId
            )
        )
    }
}
