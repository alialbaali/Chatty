package com.chatychaty.app.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogSearchBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogSearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialogSearchBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        setListeners()
        collectState()

        return binding.root
    }

    private fun setListeners() {

        binding.etUsername.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        binding.btnSearch.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.etUsername.windowToken, 0)
            findNavController().navigate(SearchDialogFragmentDirections.actionSearchDialogFragmentToProgressDialogFragment())
            viewModel.createChat()
        }
    }

    private fun collectState() {
        val parentFragmentView = requireParentFragment().requireView()
        val parentFragmentAnchorView = parentFragmentView.findViewById<FloatingActionButton>(R.id.fab)
        viewModel.state
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {
                        findNavController().navigateUp()
                        findNavController().navigateUp()
                        parentFragmentView.snackbar(state.exception.message.toString(), anchorView = parentFragmentAnchorView)
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        findNavController().navigateUp()
                        findNavController().navigateUp()
                        parentFragmentView.snackbar(state.value, anchorView = parentFragmentAnchorView)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}
