package com.chatychaty.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogFeedbackBinding
import com.chatychaty.app.util.snackbar
import com.chatychaty.app.util.toast
import org.koin.android.viewmodel.ext.android.viewModel

class FeedbackDialogFragment : BaseBottomSheetDialogFragment() {

    private val binding by lazy { FragmentDialogFeedbackBinding.inflate(layoutInflater) }

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding.btnSubmit.setOnClickListener {
            dismiss()
            val rating = "Rating is ${binding.rb.rating.toInt()}"
            it.toast(rating)
        }

        return binding.root
    }
}