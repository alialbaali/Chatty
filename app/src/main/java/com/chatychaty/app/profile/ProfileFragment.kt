package com.chatychaty.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chatychaty.app.ConfirmationDialogFragment
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentProfileBinding
import com.chatychaty.app.util.toast
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ProfileFragment
            viewModel = this@ProfileFragment.viewModel
        }

        viewModel.getUser()

        binding.tb.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvEditInfo.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditInfoDialogFragment())
        }

        binding.tvChangePassword.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPasswordDialogFragment())
        }

        binding.tvSubmitFeedback.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFeedbackDialogFragment())
        }

        binding.tvSignOut.setOnClickListener {
            showSignOutDialog()
        }

        binding.tvDeleteAccount.setOnClickListener {
            showDeleteDialog()
        }

        return binding.root
    }

    private fun showSignOutDialog() {

        ConfirmationDialogFragment { dialogBinding, dialogFragment ->

            dialogBinding.tvTitle.text = resources.getString(R.string.sign_out_desc)

            dialogBinding.btnConfirm.text = resources.getString(R.string.sign_out)

            dialogBinding.btnConfirm.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignGraph())
                dialogFragment.dismiss()
                viewModel.signOut()
            }

        }.show(parentFragmentManager, null)
    }

    private fun showDeleteDialog() {

        ConfirmationDialogFragment { dialogBinding, dialogFragment ->

            dialogBinding.tvTitle.text = resources.getString(R.string.delete_account_desc)

            dialogBinding.tvSubtitle.text = resources.getString(R.string.action_can_be_undone)

            dialogBinding.btnConfirm.text = resources.getString(R.string.delete_account)

            dialogBinding.btnConfirm.textSize = 14F

            dialogBinding.btnConfirm.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))

            dialogBinding.btnConfirm.setOnClickListener {
                dialogFragment.dismiss()
                it.toast("TODO")
            }

        }.show(parentFragmentManager, null)
    }
}