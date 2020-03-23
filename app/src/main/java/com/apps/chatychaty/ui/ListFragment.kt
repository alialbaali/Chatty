package com.apps.chatychaty.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.adapter.ListRVAdapter
import com.apps.chatychaty.databinding.FragmentListBinding
import com.apps.chatychaty.model.Chat
import com.google.android.material.transition.MaterialSharedAxis

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private lateinit var adapter: ListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)



        binding.lifecycleOwner = this

        adapter = ListRVAdapter()

        binding.rv.let { rv ->

            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.submitList(listOf(Chat(senderId = 1, receiverId = 1)))

        }

        binding.fab.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, true).apply {
                    duration = DURATION
                }

            this.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToSearchFragment())
        }

        binding.tb.let { tb ->

            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

            (tb.menu.findItem(R.id.search).actionView as SearchView).apply {
                this.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            }
        }


        return binding.root
    }

}
