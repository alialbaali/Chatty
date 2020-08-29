package com.chatychaty.app.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.databinding.FragmentSearchBinding
import com.chatychaty.app.R
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private val binding by lazy {
        FragmentSearchBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
    }

    private val viewModel by viewModel<SearchViewModel>()

    private val imm by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorPrimary))

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            this.findNavController().navigateUp()
        }

        binding.et.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        binding.et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                imm.hideSoftInputFromWindow(v.windowToken, 0)

                this.findNavController().navigateUp()
            }
            true
        }

        return binding.root
    }
}
