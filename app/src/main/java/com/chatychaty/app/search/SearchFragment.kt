package com.chatychaty.app.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentSearchBinding
import com.chatychaty.app.util.ProgressDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var progressDialogFragment: ProgressDialogFragment

    private val imm by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorPrimary))

        setListeners()
        collectState()

        return binding.root
    }

    private fun setListeners() {

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            findNavController().navigateUp()
        }

        binding.et.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        binding.et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                viewModel.createChat()

                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun collectState() {
        viewModel.state
            .onEach {
                when (it) {
                    is UiState.Empty -> {

                    }
                    is UiState.Failure -> {
                        progressDialogFragment.dismiss()
                        binding.root.snackbar(it.exception.message.toString())
                    }
                    is UiState.Loading -> {
                        progressDialogFragment = ProgressDialogFragment()
                            .also { it.show(parentFragmentManager, null) }
                    }
                    is UiState.Success -> {
                        progressDialogFragment.dismiss()
                        findNavController().navigateUp()
                        binding.root.snackbar(it.value)
                    }
                }
            }
            .launchIn(lifecycleScope)

    }
}
