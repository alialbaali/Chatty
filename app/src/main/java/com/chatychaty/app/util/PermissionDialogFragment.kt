package com.chatychaty.app.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatychaty.app.databinding.FragmentDialogPermissionBinding

class PermissionDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogPermissionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialogPermissionBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }

}