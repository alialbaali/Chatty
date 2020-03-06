package com.apps.chatychaty.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.apps.chatychaty.databinding.FragmentChatBinding
import com.apps.chatychaty.viewModel.ChatViewModel

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel by viewModels <ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root
    }


}
