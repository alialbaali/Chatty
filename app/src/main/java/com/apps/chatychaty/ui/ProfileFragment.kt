package com.apps.chatychaty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.FragmentProfileBinding
import com.google.android.material.transition.MaterialSharedAxis
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)



        binding.tb.setNavigationOnClickListener {

            enterTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true).apply {
                    duration = DURATION
                }

            this.findNavController().navigateUp()
        }
        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorOnPrimary_900))

        activity?.getPreferences(Context.MODE_PRIVATE).let {
            val name = it?.getString("name", null)
            val username = it?.getString("username", null)

            Timber.i(it?.getString("img_url", null).plus(name))
            binding.name.setText(name)
            binding.username.text = username
        }

        val menuEditItem = binding.tb.menu.findItem(R.id.edit)

        val menuDoneItem = binding.tb.menu.findItem(R.id.done)

        menuEditItem.icon.setTint(resources.getColor(R.color.colorOnPrimary_900))

        menuDoneItem.icon.setTint(resources.getColor(R.color.colorOnPrimary_900))

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        menuEditItem.setOnMenuItemClickListener {
            it.isVisible = false

            menuDoneItem.isVisible = true

            binding.name.isEnabled = true

            binding.name.requestFocus()

            binding.name.setSelection(binding.name.text.length)

            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )


            true
        }

        menuDoneItem.setOnMenuItemClickListener {
            it.isVisible = false
            menuEditItem.isVisible = true

            binding.name.isEnabled = false

            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)

            true
        }


        return binding.root
    }

}
