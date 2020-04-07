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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.*
import com.apps.chatychaty.adapter.ChatRVAdapter
import com.apps.chatychaty.databinding.FragmentChatBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.util.ExceptionHandler
import com.apps.chatychaty.util.getPref
import com.apps.chatychaty.util.snackbar
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.SharedViewModel
import com.apps.chatychaty.viewModel.SharedViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment(), Error {

    private val binding by lazy {
        FragmentChatBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
        }
    }

    private val viewModel by viewModels<SharedViewModel> {
        SharedViewModelFactory(Repos.chatRepository, Repos.messageRepository, this)
    }

    private val args by navArgs<ChatFragmentArgs>()

    private val adapter by lazy { ChatRVAdapter(activity?.getPref("username")!!) }

    private val imm by lazy {
        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ExceptionHandler.error = this

        enterTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true).apply {
                duration = DURATION
            }

        exitTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, false).apply {
                duration = DURATION
            }

        // Binding
        binding.viewModel = viewModel

        binding.tbTv.text = args.name

        Glide.with(this)
            .load(args.imgUrl)
            .placeholder(resources.getDrawable(R.drawable.ic_person_24dp, null))
            .circleCrop()
            .into(binding.img)

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

        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorOnPrimary_900))

        binding.et.requestFocus()

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            this.findNavController().navigateUp()
        }

        binding.img.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)

            enterTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, false)

            exitTransition = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, true)

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
        val enterAnimation = MaterialFade.create(requireContext(), true).apply {
            duration = DURATION
        }

        val exitAnimation = MaterialFade.create(requireContext(), false).apply {
            duration = DURATION
        }

        if (binding.et.text.isBlank()) {
            TransitionManager.beginDelayedTransition(binding.linearLayout, exitAnimation)
            binding.btnSend.isEnabled = false
            binding.btnSend.visibility = View.GONE
        }
        binding.et.addTextChangedListener {
            if (it?.isNotBlank() == true) {
                TransitionManager.beginDelayedTransition(binding.linearLayout, enterAnimation)
                binding.btnSend.isEnabled = true
                binding.btnSend.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(binding.linearLayout, exitAnimation)
                binding.btnSend.isEnabled = false
                binding.btnSend.visibility = View.GONE
            }
        }

        binding.et.setOnClickListener {

            binding.et.requestFocus()
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        return binding.root
    }

    override fun snackbar(value: String) {
        binding.cool.snackbar(value)
    }
}
