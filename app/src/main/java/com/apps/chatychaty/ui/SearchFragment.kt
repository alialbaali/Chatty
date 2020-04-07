package com.apps.chatychaty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.R
import com.apps.chatychaty.adapter.SearchAdapter
import com.apps.chatychaty.databinding.FragmentSearchBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.SearchViewModel
import com.apps.chatychaty.viewModel.SearchViewModelFactory
import com.google.android.material.transition.MaterialSharedAxis

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModels<SearchViewModel> { SearchViewModelFactory(Repos.userRepository) }

    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        enterTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, true)

        exitTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

//        adapter = SearchAdapter()

        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorOnPrimary_900))

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.tb.setNavigationOnClickListener {
            imm.hideSoftInputFromWindow(binding.et.windowToken, 0)
            this.findNavController().navigateUp()
        }

        binding.et.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

//
//        binding.rv.let { rv ->
//
//            rv.adapter = adapter
//
//            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//            viewModel.users.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    adapter.submitList(it)
//                }
//            })
//
//        }

        binding.et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                imm.hideSoftInputFromWindow(v.windowToken, 0)

                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToListFragment(viewModel.username.value!!))

            }
            true
        }

        return binding.root
    }
}
