package com.apps.chatychaty.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.adapter.NavigateToChat
import com.apps.chatychaty.adapter.SearchAdapter
import com.apps.chatychaty.databinding.FragmentSearchBinding
import com.apps.chatychaty.databinding.FragmentSearchBinding.inflate
import com.apps.chatychaty.model.User
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.SearchViewModel
import com.apps.chatychaty.viewModel.SearchViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), NavigateToChat {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModels<SearchViewModel> { SearchViewModelFactory(Repos.userRepository) }

    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        adapter = SearchAdapter()

        binding.tb.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }

        binding.rv.let { rv ->

            rv.adapter = adapter

            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.users.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })

        }

        binding.et.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    v.windowToken, 0
                )
                viewModel.getUsers()
            }
            true
        }

        return binding.root
    }

    override fun navigate(user: User) {
        TODO("Navigate to a new chat with the provided user")
    }

}
