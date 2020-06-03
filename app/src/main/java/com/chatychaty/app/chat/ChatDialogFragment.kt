package com.chatychaty.app.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.SharedViewModel
import com.chatychaty.app.databinding.FragmentDialogChatBinding
import com.chatychaty.app.util.toast
import org.koin.android.viewmodel.ext.android.viewModel

class ChatDialogFragment : BaseBottomSheetDialogFragment() {

    private val binding by lazy { FragmentDialogChatBinding.inflate(layoutInflater) }

    private val args by navArgs<ChatDialogFragmentArgs>()

    private val viewModel by viewModel<SharedViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.getChatById(args.chatId)

        viewModel.chat.observe(viewLifecycleOwner, Observer { chat ->
            binding.tvViewProfile.text = "View ${chat.user.name}'s Profile"
        })

        binding.tvArchiveChat.setOnClickListener {
            dismiss()
            it.toast("TODO")
        }

        binding.tvBlockUser.setOnClickListener {
            dismiss()
            it.toast("TODO")
        }

        binding.tvChangeColor.setOnClickListener {
            dismiss()
            it.toast("TODO")
        }

        binding.tvDeleteChat.setOnClickListener {
            dismiss()
            it.toast("TODO")
        }

        binding.tvViewProfile.setOnClickListener {
            dismiss()
            findNavController().navigate(ChatDialogFragmentDirections.actionChatDialogFragmentToProfileFragment(args.chatId))
        }

        return binding.root
    }

}