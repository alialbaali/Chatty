package com.apps.chatychaty.ui


import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.adapter.ChatRVAdapter
import com.apps.chatychaty.databinding.FragmentChatBinding
import com.apps.chatychaty.repo.USERNAME
import com.apps.chatychaty.util.getPref
import com.apps.chatychaty.util.snackbar
import com.apps.chatychaty.viewModel.SharedViewModel
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private val binding by lazy {
        FragmentChatBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
        }
    }

    private val viewModel by viewModel<SharedViewModel>()

    private val args by navArgs<ChatFragmentArgs>()

    private val adapter by lazy { ChatRVAdapter(requireContext().getPref(USERNAME)!!) }

    private val imm by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        enterTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.X, true).apply {
                duration = DURATION
            }

        exitTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.X, false).apply {
                duration = DURATION
            }

        // Binding
        binding.viewModel = viewModel

        binding.tbTv.text = args.name

        binding.imgUrl = args.imgUrl

        // RV
        binding.rv.let { rv ->

            viewModel.getLiveDataMessages(args.chatId)

            rv.adapter = adapter
            rv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
                    stackFromEnd = true
                }

            viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
                messages.let {

                    adapter.submitList(messages)

                    if (messages.isNotEmpty()) {
                        binding.rv.smoothScrollToPosition(messages.size.minus(1))
                    }
                }
            })

        }

        // Send Button
        binding.btnSend.let { btnSend ->
            btnSend.setOnClickListener {
                viewModel.insertMessage(args.chatId)
            }
        }

        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorPrimary))

        binding.et.requestFocus()

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            this.findNavController().navigateUp()
        }

        binding.img.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)

            enterTransition =
                MaterialSharedAxis.create(MaterialSharedAxis.Y, false)

            exitTransition = MaterialSharedAxis.create(MaterialSharedAxis.Y, true)

            this.findNavController().navigate(
                ChatFragmentDirections.actionChatFragmentToProfileFragment(
                    args.name,
                    args.username,
                    args.imgUrl,
                    args.chatId
                )
            )
        }

        viewModel.let { viewModel ->
            lifecycleScope.launch(Dispatchers.IO) {

                while (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {

                    delay(1500)

                    val username = activity?.getPref("username")!!

                    viewModel.isMessageDelivered(username)

                }
            }
        }
        val enterAnimation = MaterialFade.create(true).apply {
            duration = DURATION
        }

        val exitAnimation = MaterialFade.create(false).apply {
            duration = DURATION
        }
        if (binding.et.text.isBlank()) {
            binding.btnSend.alpha = 0.5f
            binding.btnSend.isEnabled = false
            TransitionManager.beginDelayedTransition(binding.linearLayout, exitAnimation)
        }
        binding.et.addTextChangedListener {
            if (it?.isNotBlank() == true) {
                TransitionManager.beginDelayedTransition(binding.linearLayout, enterAnimation)
                binding.btnSend.isEnabled = true
//                binding.btnSend.visibility = View.VISIBLE
                binding.btnSend.alpha = 1.0f
            } else {
                TransitionManager.beginDelayedTransition(binding.linearLayout, exitAnimation)
                binding.btnSend.isEnabled = false
//                binding.btnSend.visibility = View.INVISIBLE
                binding.btnSend.alpha = 0.5f
            }
        }

        binding.et.setOnClickListener {

            binding.et.requestFocus()
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        viewModel.errors.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.cool.snackbar(it)
            }
        })
        return binding.root
    }
}
