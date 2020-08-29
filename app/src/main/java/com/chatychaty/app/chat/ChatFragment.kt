package com.chatychaty.app.chat


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatychaty.app.SharedViewModel
import com.chatychaty.app.databinding.FragmentChatBinding
import com.chatychaty.app.util.getPref
import com.chatychaty.domain.model.Message
import com.chatychaty.domain.repository.USERNAME
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel by viewModel<SharedViewModel>()

    private val args by navArgs<ChatFragmentArgs>()

    private val rvAdapter by lazy { MessageRVAdapter(requireContext().getPref(USERNAME)!!, messageItemListener) }

    private val imm by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val messageItemListener by lazy {
        object : MessageItemListener {
            override fun onTouch(message: Message) {
                findNavController().navigate(ChatFragmentDirections.actionChatFragmentToMessageDialogFragment(args.chatId, message.messageId))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ChatFragment
            viewModel = this@ChatFragment.viewModel
        }

        viewModel.getChatById(args.chatId)

        with(binding.rv) {

            viewModel.getMessages(args.chatId)

            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply { stackFromEnd = true }

            viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
                messages?.let {

                    rvAdapter.submitList(messages)

                    if (messages.isNotEmpty()) {
                        binding.rv.smoothScrollToPosition(messages.size.minus(1))
                    }
                }
            })

        }

        binding.btnSend.let { btnSend ->
            btnSend.setOnClickListener {
                viewModel.createMessage(args.chatId)
//                binding.rv.adapter?.notifyItemInserted(viewModel.messages.value!!.size)
            }
        }

        binding.et.requestFocus()

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            this.findNavController().navigateUp()
        }

        binding.img.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)

            findNavController().navigate(ChatFragmentDirections.actionChatFragmentToProfileFragment(args.chatId))
        }

//        viewModel.let { viewModel ->
//            lifecycleScope.launch(Dispatchers.IO) {
//
//                while (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
//
//                    delay(1500)
//
//                    val username = activity?.getPref("username")!!
//
//                    viewModel.isMessageDelivered(username)
//
//                }
//            }
//        }
//        if (binding.et.text.isBlank()) {
////            binding.btnSend.alpha = 0.5f
//            binding.btnSend.isEnabled = false
//            TransitionManager.beginDelayedTransition(binding.linearLayout, exitAnimation)
//        }
//        binding.et.addTextChangedListener {
//            if (it?.isNotBlank() == true) {
//                TransitionManager.beginDelayedTransition(binding.linearLayout, enterAnimation)
//                binding.btnSend.isEnabled = true
//                binding.btnSend.visibility = View.VISIBLE
////                binding.btnSend.alpha = 1.0f
//            } else {
//                TransitionManager.beginDelayedTransition(binding.linearLayout, exitAnimation)
//                binding.btnSend.isEnabled = false
//                binding.btnSend.visibility = View.INVISIBLE
////                binding.btnSend.alpha = 0.5f
//            }
//        }

        binding.et.setOnClickListener {

            binding.et.requestFocus()
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        binding.ibMenu.setOnClickListener {
            findNavController().navigate(ChatFragmentDirections.actionChatFragmentToChatDialogFragment(args.chatId))
        }

//        viewModel.errors.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                binding.cool.snackbar(it)
//            }
//        })
        return binding.root
    }
}
