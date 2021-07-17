package com.chatychaty.app.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogNameBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NameDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogNameBinding

    private val viewModel by viewModel<UserViewModel>()

    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialogNameBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.etName.requestFocus()
        imm.showSoftInput(binding.etName, InputMethodManager.SHOW_IMPLICIT)

        collectState()
        setupListeners()

        return binding.root
    }

    private fun collectState() {
        viewModel.state
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {
                        findNavController().navigateUp()
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        findNavController().navigateUp()
                        requireParentFragment()
                            .requireView()
                            .snackbar("Name has changed successfully.")
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            if (binding.etName.text.isNullOrBlank()) {
                binding.tilName.error = getString(R.string.name_empty)
            } else {
                findNavController().navigate(NameDialogFragmentDirections.actionNameDialogFragmentToProgressDialogFragment())
                viewModel.updateName()
            }
        }
    }
}