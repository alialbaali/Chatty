package com.chatychaty.app.message


import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.databinding.FragmentListMessageBinding
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.cancelNotification
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MessageListFragment : Fragment() {

    private lateinit var binding: FragmentListMessageBinding

    private val viewModel by viewModel<MessageListViewModel> { parametersOf(args.chatId) }

    private val args by navArgs<MessageListFragmentArgs>()

    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    private val notificationManager by lazy { requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val messageItemListener by lazy {
        MessageListAdapter.MessageItemListener { message ->
            findNavController().navigate(
                MessageListFragmentDirections.actionMessageListFragmentToMessageDialogFragment(
                    args.chatId,
                    message.id
                )
            )
        }
    }

    private lateinit var adapter: MessageListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentListMessageBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MessageListFragment
            viewModel = this@MessageListFragment.viewModel
        }

        collectState()
        setupListeners()

        notificationManager.cancelNotification(args.chatId)
        binding.et.requestFocus()

        return binding.root
    }

    private fun setupListeners() {
        binding.ivImage.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            findNavController().navigate(MessageListFragmentDirections.actionMessageListFragmentToProfileFragment(args.chatId))
        }

        binding.et.setOnClickListener {

            binding.et.requestFocus()

            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        binding.ibMenu.setOnClickListener {
            findNavController().navigate(MessageListFragmentDirections.actionMessageListFragmentToMessageListDialogFragment(args.chatId))
        }

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            findNavController().navigateUp()
        }
        binding.btnSend.setOnClickListener {
            viewModel.createMessage()
            val insertedMessagePosition = (viewModel.messages.value as UiState.Success).value.size
            adapter.notifyItemInserted(insertedMessagePosition)
            binding.et.setText("")
        }

    }

    private fun collectState() {
        viewModel.chat
            .onEach { state ->
                when (state) {
                    is UiState.Empty -> {

                    }
                    is UiState.Failure -> {

                    }
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        val chat = state.value
                        adapter = MessageListAdapter(chat.username, messageItemListener)
                        binding.rv.adapter = adapter
                        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            .apply { stackFromEnd = true }
                        binding.imgUrl = chat.imageUrl
                        binding.tvAppName.text = chat.name
                    }
                }
            }
            .launchIn(lifecycleScope)

        viewModel.messages
            .onEach { state ->
                when (state) {
                    is UiState.Empty -> {
                    }
                    is UiState.Failure -> {
                    }
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        val messages = state.value

                        if (messages.isEmpty()) {
                            binding.tvPlaceholder.visibility = View.VISIBLE
                            binding.rv.visibility = View.GONE
                        } else {
                            if (this::adapter.isInitialized)
                                adapter.submitList(messages)
                            binding.tvPlaceholder.visibility = View.GONE
                            binding.rv.visibility = View.VISIBLE
                            binding.rv.smoothScrollToPosition(state.value.size.minus(1))
                        }

                    }
                }
            }
            .launchIn(lifecycleScope)

        viewModel.messageBody
            .onEach { binding.btnSend.isEnabled = it.isNotBlank() }
            .launchIn(lifecycleScope)

        viewModel.isSearchEnabled
            .onEach {
                if (binding.etSearch.visibility == View.VISIBLE)
                    disableSearch()
                else
                    enableSearch()
            }
            .launchIn(lifecycleScope)
    }

    private fun enableSearch() {
        binding.tilSearch.visibility = View.GONE
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.tilSearch.visibility = View.VISIBLE
        binding.etSearch.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        viewModel.searchTerm
            .onEach { term ->
                val messages = (viewModel.messages.value as UiState.Success)
                messages.value
                    .indexOfFirst { it.body.contains(term, ignoreCase = true) }
                    .plus(1)
                    .also { messageIndex -> binding.rv.smoothScrollToPosition(messageIndex) }

                if (term.isBlank())
                    binding.rv.smoothScrollToPosition(messages.value.size)
            }
            .launchIn(lifecycleScope)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                disableSearch()
                if (isEnabled) isEnabled = false
            }
    }

    private fun disableSearch() {
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.tilSearch.visibility = View.GONE
        binding.etSearch.setText("")
    }

}