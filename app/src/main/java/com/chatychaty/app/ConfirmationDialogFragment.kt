package com.chatychaty.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.databinding.FragmentDialogConfirmationBinding

class ConfirmationDialogFragment(private val block: (dialogBinding: FragmentDialogConfirmationBinding, dialogFragment: ConfirmationDialogFragment) -> Unit) : BaseBottomSheetDialogFragment() {

    private val binding by lazy { FragmentDialogConfirmationBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        block(binding, this)
        if (binding.tvSubtitle.text.isNullOrBlank()) binding.tvSubtitle.visibility = View.GONE
        return binding.root
    }

}