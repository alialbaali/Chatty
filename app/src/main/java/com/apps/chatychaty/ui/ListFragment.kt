package com.apps.chatychaty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.DURATION
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

        binding.img.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true).apply {
                    duration = DURATION
                }

            this.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToProfileFragment())
        }



        binding.fab.setOnClickListener {

            exitTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, true).apply {
                    duration = DURATION
                }

            this.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToSearchFragment())
        }

        return binding.root
    }

}
