package com.chatychaty.app.list

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.R
import com.chatychaty.app.SharedViewModel
import com.chatychaty.app.databinding.FragmentListBinding
import com.chatychaty.app.util.snackbar
import com.chatychaty.app.util.toast
import com.chatychaty.domain.model.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ListFragment : Fragment(), ChatItemListener {

    private lateinit var binding: FragmentListBinding

    private val rvAdapter by lazy { ListRVAdapter(viewModel, this) }

    private val rvPinnedChatAdapter by lazy { PinnedChatRVAdapter() }

    private val viewModel by viewModel<SharedViewModel>()

    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentListBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ListFragment
        }

        binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorBackground))

        with(binding.rv) {

            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.chats.observe(viewLifecycleOwner, Observer { chats ->
                rvAdapter.submitList(chats)
            })

//            ItemTouchHelper(ChatItemTouchHelper(rvAdapter, requireContext())).attachToRecyclerView(rv)
        }

//        with(binding.rvPinned) {
//
//            adapter = rvPinnedChatAdapter
//            layoutManager = GridLayoutManager(requireContext(), 3)
//
//            viewModel.chats.observe(viewLifecycleOwner, Observer { chats ->
//                rvPinnedChatAdapter.submitList(chats)
//            })
//
//        }

//        binding.bab.setOnMenuItemClickListener { item ->
//
//            when (item.itemId) {
//                R.id.search -> {
//
//                    binding.tilSearch.visibility = View.VISIBLE
//
//                    binding.rv.visibility = View.GONE
//
//                    imm.showSoftInput(binding.etSearch, 0)
//
//                    viewModel.searchTerm.observe(viewLifecycleOwner, Observer { searchTerm ->
//                        TODO("Filter the list to match the search term")
//                    })
//
//                }
//            }
//            false
//        }

        binding.img.setOnClickListener {

            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToProfileFragment(0)
            )

        }

        binding.fab.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToSearchFragment())
        }

        viewModel.let { viewModel ->
            lifecycleScope.launch(Dispatchers.IO) {

                whenStarted {
                    viewModel.getRemoteChats()
                    viewModel.getRemoteMessages()
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


        binding.bab.setNavigationOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToListDialogFragment())
        }

        binding.bab.setOnMenuItemClickListener { item ->
            view?.toast("TOOD")
            true
        }

        return binding.root
    }

    override fun navigateToChat(chat: Chat) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToChatFragment(chat.chatId))
    }

    override fun navigateToProfile(chat: Chat) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToProfileFragment(chat.chatId))
    }

    override fun navigateToChatDialogFragment(chat: Chat) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToChatDialogFragment(chat.chatId))
    }
}
