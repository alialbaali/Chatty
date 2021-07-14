package com.chatychaty.app.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogFeedbackBinding
import com.chatychaty.app.util.toast
import org.koin.android.viewmodel.ext.android.viewModel

class FeedbackDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogFeedbackBinding

    private val viewModel by viewModel<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentDialogFeedbackBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@FeedbackDialogFragment
        }

        binding.btnSubmit.setOnClickListener {
            dismiss()
            val rating = "Rating is ${binding.rb.rating.toInt()}"
            it.toast(rating)
        }

        return binding.root
    }
}