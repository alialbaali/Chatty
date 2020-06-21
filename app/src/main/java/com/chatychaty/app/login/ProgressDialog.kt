package com.chatychaty.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogProgressBinding

class ProgressDialog : BaseBottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentDialogProgressBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@ProgressDialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        isCancelable = false

        return binding.root
    }

}