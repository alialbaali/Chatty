package com.chatychaty.app.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.SharedViewModel
import com.chatychaty.app.databinding.FragmentDialogMessageBinding
import com.chatychaty.app.util.toast
import com.chatychaty.domain.model.Message
import org.koin.android.viewmodel.ext.android.viewModel

class MessageDialogFragment : BaseBottomSheetDialogFragment() {

    private val binding by lazy { FragmentDialogMessageBinding.inflate(layoutInflater) }

    private val args by navArgs<MessageDialogFragmentArgs>()

    private val viewModel by viewModel<SharedViewModel>()

    private val clipboardManager by lazy { requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    private lateinit var message: Message

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.getMessages(args.chatId)

        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            messages.firstOrNull { it.messageId == args.messageId }?.let {
                message = it
            }
        })


        binding.tvCopy.setOnClickListener { view ->

            dismiss()

            message.let { message ->

                val label = "${message.messageId} ${message.username}"

                val text = message.messageBody

                val clipData = ClipData.newPlainText(label, text)

                clipboardManager.setPrimaryClip(clipData)

                view.toast("Copied to clipboard")
            }

        }

        binding.tvForward.setOnClickListener {

            dismiss()

            findNavController().navigate(MessageDialogFragmentDirections.actionMessageDialogFragmentToChatsDialogFragment())

        }

        binding.tvShare.setOnClickListener {

            dismiss()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_MIME_TYPES, message.username)
                putExtra(Intent.EXTRA_MIME_TYPES, message.messageBody)
            }

            val chooser = Intent.createChooser(intent, "Share to")

            startActivity(chooser)

        }



        return binding.root
    }

}