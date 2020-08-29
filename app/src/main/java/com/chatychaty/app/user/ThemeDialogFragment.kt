package com.chatychaty.app.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogThemeBinding
import com.chatychaty.domain.repository.DARK_THEME
import com.chatychaty.domain.repository.SYSTEM_DEFAULT
import com.chatychaty.domain.repository.LIGHT_THEME
import org.koin.android.viewmodel.ext.android.viewModel

class ThemeDialogFragment : BaseBottomSheetDialogFragment() {

    private val binding by lazy { FragmentDialogThemeBinding.inflate(layoutInflater) }

    private val viewModel by viewModel<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.theme.observe(viewLifecycleOwner, Observer { theme ->

            when (theme ?: SYSTEM_DEFAULT) {
                SYSTEM_DEFAULT -> binding.rbSystemDefault.isChecked = true
                LIGHT_THEME -> binding.rbLightTheme.isChecked = true
                DARK_THEME -> binding.rbDarkTheme.isChecked = true
            }

        })

        binding.rbLightTheme.setOnClickListener {
            dismiss()
            viewModel.setThemeValue(LIGHT_THEME)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.rbDarkTheme.setOnClickListener {
            dismiss()
            viewModel.setThemeValue(DARK_THEME)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.rbSystemDefault.setOnClickListener {
            dismiss()
            viewModel.setThemeValue(SYSTEM_DEFAULT)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        return binding.root
    }

}