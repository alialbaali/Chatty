package com.chatychaty.app.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogListBinding
import com.chatychaty.app.util.toast

class ListDialogFragment : BaseBottomSheetDialogFragment() {


    private val binding by lazy { FragmentDialogListBinding.inflate(layoutInflater) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding.tvArchivedChats.setOnClickListener {
            dismiss()
            it.toast("TODO")
        }

        binding.tvInviteOthers.setOnClickListener {
            dismiss()
            it.toast("TODO")
        }

        binding.tvViewProfile.setOnClickListener {
            findNavController().navigate(ListDialogFragmentDirections.actionListDialogFragmentToProfileFragment(0))
        }

        return binding.root
    }

}