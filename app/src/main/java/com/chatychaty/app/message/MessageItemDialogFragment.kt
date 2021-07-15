package com.chatychaty.app.message

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.navArgs
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogMessageBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageItemDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogMessageBinding

    private val args by navArgs<MessageItemDialogFragmentArgs>()

    private val viewModel by viewModel<MessageItemViewModel>()

    private val clipboardManager by lazy { requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentDialogMessageBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }

        viewModel.selectMessage(args.messageId)

        binding.tvCopy.setOnClickListener {
            dismiss()

            (viewModel.selectedMessage.value as UiState.Success).also { message ->
                val label = message.value.username
                val text = message.value.body
                val clipData = ClipData.newPlainText(label, text)

                clipboardManager.setPrimaryClip(clipData)
                requireParentFragment()
                    .requireView()
                    .snackbar(
                        getString(R.string.copied_to_clipboard),
                        anchorView = requireParentFragment()
                            .requireView()
                            .findViewById<LinearLayout>(R.id.ll)
                    )
            }
        }

        binding.tvShare.setOnClickListener {
            dismiss()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                (viewModel.selectedMessage.value as UiState.Success).also { message ->
                    putExtra(
                        Intent.EXTRA_TEXT,
                        """
                        ${message.value.username.replaceFirstChar { it.uppercase() }}:
                        
                        ${message.value.body}
                    """.trimIndent()
                    )
                }
            }
            val chooser = Intent.createChooser(intent, getString(R.string.share_to))
            startActivity(chooser)
        }

        return binding.root
    }

}