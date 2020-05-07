package com.alialbaali.chatychaty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alialbaali.chatychaty.R
import com.alialbaali.chatychaty.databinding.FragmentSearchBinding
import com.alialbaali.chatychaty.viewModel.SearchViewModel
import com.alialbaali.chatychaty.ui.SearchFragmentDirections
import com.google.android.material.transition.MaterialSharedAxis
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
        enterTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.Y, true)

        exitTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.Y, false)

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

                this.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToListFragment(viewModel.username.value!!))
            }
            true
        }

        return binding.root
    }
}
