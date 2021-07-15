package com.chatychaty.app.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.databinding.FragmentDialogProgressBinding

class ProgressDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentDialogProgressBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialogProgressBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }

        isCancelable = false

        return binding.root
    }

}