package com.apps.chatychaty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.adapter.ListRVAdapter
import com.apps.chatychaty.databinding.FragmentListBinding

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

        }

        binding.fab.setOnClickListener {
            this.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToSearchFragment())
        }


        return binding.root
    }

}
