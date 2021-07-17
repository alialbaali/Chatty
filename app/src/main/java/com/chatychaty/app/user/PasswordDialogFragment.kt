package com.chatychaty.app.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.databinding.FragmentDialogPasswordBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogPasswordBinding

    private val viewModel by viewModel<UserViewModel>()

    private val imm by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentDialogPasswordBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.etCurrentPassword.requestFocus()

        imm.showSoftInput(binding.etCurrentPassword, InputMethodManager.SHOW_IMPLICIT)

        setupListeners()
        collectState()

        return binding.root
    }

    private fun collectState() {
        viewModel.state
            .onEach { state ->
                when (state) {

                    is UiState.Failure -> {
                        findNavController().navigateUp()
                        requireParentFragment().requireView().snackbar("Password failed updating successfully")
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        findNavController().navigateUp()
                        requireParentFragment().requireView().snackbar("Password has changed successfully!")
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {

            val newPassword = binding.etNewPassword.text.toString()
            val newPasswordConfirm = binding.etNewPasswordConfirm.text.toString()

            if (newPassword != newPasswordConfirm) {
                binding.tilNewPassword.error = "Passwords don't match"
                binding.tilNewPasswordConfirm.error = "Passwords don't match"
            } else {
                imm.hideSoftInputFromWindow(binding.etCurrentPassword.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
                viewModel.updatePassword()
            }
        }
    }
}