package com.chatychaty.app.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogListChatBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment

class ChatListDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogListChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentDialogListChatBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }

        binding.tvArchivedChats.setOnClickListener {
            dismiss()
            findNavController().navigate(ChatListDialogFragmentDirections.actionListDialogFragmentToChatListArchiveFragment())
        }

        binding.tvInviteOthers.setOnClickListener {
            dismiss()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT, """
                    ${getString(R.string.invite_text)}: https://github.com/alialbaali/ChatyChaty/releases
                """.trimIndent()
                )
            }

            val chooser = Intent.createChooser(intent, getString(R.string.invite_others))

            startActivity(chooser)
        }

        binding.tvSettings.setOnClickListener {
            dismiss()
            findNavController().navigate(ChatListDialogFragmentDirections.actionListDialogFragmentToUserFragment())
        }

        binding.tvTheme.setOnClickListener {
            dismiss()
            findNavController().navigate(ChatListDialogFragmentDirections.actionListDialogFragmentToThemeDialogFragment())
        }

        return binding.root
    }

}