package com.apps.chatychaty.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.chatychaty.adapter.ChatRVAdapter
import com.apps.chatychaty.databinding.FragmentChatBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.ChatViewModel
import com.apps.chatychaty.viewModel.ChatViewModelFactory
import com.apps.chatychaty.viewModel.Error
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment(), Error {

    private lateinit var binding: FragmentChatBinding

    private val viewModel by viewModels<ChatViewModel> {
        ChatViewModelFactory(Repos.messageRepository)
    }

    private lateinit var adapter: ChatRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.getPreferences(Context.MODE_PRIVATE).let {

            it?.let {
                val username = it.getString("username", null)!!
                viewModel.currentMessage.value?.user = username
                viewModel.username = username
                viewModel.token.value = it.getString("token", null)!!
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        adapter = ChatRVAdapter()

        // Binding
        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        // RV
        binding.rv.let { rv ->
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
                messages.let {
                    adapter.submitList(messages)
                    rv.smoothScrollToPosition(messages.size.minus(1))
                }
            })

        }

        // Send Button
        binding.btnSend.let { btnSend ->
            btnSend.setOnClickListener {
                viewModel.postMessage()
            }
        }


        // ViewModel
        viewModel.let { viewModel ->

            viewModel.error = this

            lifecycleScope.launchWhenResumed {
                while (this@ChatFragment.isResumed) {
                    delay(1000)
                    viewModel.getMessages()
                    adapter.notifyDataSetChanged()
                }
            }
        }

        return binding.root
    }

    override fun snackbar(value: String) {
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG).show()
    }


}
