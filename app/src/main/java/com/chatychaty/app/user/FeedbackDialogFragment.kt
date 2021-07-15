package com.chatychaty.app.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogFeedbackBinding
import com.chatychaty.app.util.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedbackDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogFeedbackBinding

    private val viewModel by viewModel<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentDialogFeedbackBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            dismiss()
            val rating = binding.rb.rating.toInt().toString()
            requireParentFragment().requireView().snackbar(
                "Rating is $rating, " +
                        "Feedback is ${binding.etFeedback.text.toString()}"
            )
        }
    }
}