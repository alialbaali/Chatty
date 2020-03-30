package com.apps.chatychaty.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.adapter.ChatRVAdapter
import com.apps.chatychaty.databinding.FragmentChatBinding
import com.apps.chatychaty.getPref
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.snackbar
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.SharedViewModel
import com.apps.chatychaty.viewModel.SharedViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.transition.MaterialSharedAxis

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment(), Error {

    private lateinit var binding: FragmentChatBinding

    private val viewModel by viewModels<SharedViewModel> {
        SharedViewModelFactory(Repos.chatRepository, Repos.messageRepository, this)
    }

    private val args by navArgs<ChatFragmentArgs>()

    private lateinit var adapter: ChatRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        enterTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Z, true).apply {
                duration = DURATION
            }

        adapter = ChatRVAdapter(activity?.getPref("username")!!)

        // Binding
        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.tbTv.text = args.name

        Glide.with(this)
            .load(args.imgUrl)
            .placeholder(resources.getDrawable(R.drawable.img_background, null))
            .circleCrop()
            .apply(RequestOptions.overrideOf(120, 120))
            .into(binding.img)

        // RV
        binding.rv.let { rv ->

            viewModel.getLiveDataMessages(args.chatId)

            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.smoothScrollToPosition(viewModel.messages.value?.size?.minus(1) ?: 0)

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
            this.findNavController().navigateUp()
        }

        binding.img.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true).apply {
                    duration = DURATION
                }

            this.findNavController().navigate(
                ChatFragmentDirections.actionChatFragmentToProfileFragment(
                    args.name,
                    args.username,
                    args.imgUrl,
                    args.chatId
                )
            )
        }
        binding.root.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.rv.smoothScrollToPosition(viewModel.messages.value?.size?.minus(1) ?: 0)
        }

        return binding.root
    }

    override fun snackbar(value: String) {
        binding.cool.snackbar(value)
    }
}
