package com.chatychaty.app.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogPasswordBinding
import com.chatychaty.app.util.toast
import org.koin.android.viewmodel.ext.android.viewModel

class PasswordDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogPasswordBinding

    private val viewModel by viewModel<ProfileViewModel>()

    private val imm by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentDialogPasswordBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@PasswordDialogFragment
            viewModel = this@PasswordDialogFragment.viewModel
        }

        binding.etCurrentPassword.requestFocus()

        imm.showSoftInput(binding.etCurrentPassword, InputMethodManager.SHOW_IMPLICIT)

        binding.btnConfirm.setOnClickListener {

            val newPassword = binding.etNewPassword.text.toString()
            val newPasswordConfirm = binding.etNewPasswordConfirm.text.toString()

            if (newPassword != newPasswordConfirm) {

                binding.tilNewPassword.error = "Passwords don't match"
                binding.tilNewPasswordConfirm.error = "Passwords don't match"

            } else {

                imm.hideSoftInputFromWindow(binding.etCurrentPassword.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)

                dismiss()

                viewModel.changePassword()

                it.toast("TODO")

            }
        }

        return binding.root
    }
}