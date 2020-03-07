package com.apps.chatychaty.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.adapter.ChatRVAdapter
import com.apps.chatychaty.databinding.FragmentChatBinding
import com.apps.chatychaty.viewModel.ChatViewModel

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel by viewModels<ChatViewModel>()

    private lateinit var adapter: ChatRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        adapter = ChatRVAdapter()

        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.rv.let { rv ->
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
                messages.let {
                    adapter.submitList(messages)
                }
            })
        }

        binding.btnSend.let { btnSend ->
            btnSend.setOnClickListener {
                viewModel.insertNote()
                adapter.notifyDataSetChanged()
            }
        }

        return binding.root
    }


}
