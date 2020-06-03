package com.chatychaty.app.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.databinding.FragmentDialogThemeBinding
import com.chatychaty.domain.repository.DARK_THEME
import com.chatychaty.domain.repository.FOLLOW_SYSTEM_THEME
import com.chatychaty.domain.repository.LIGHT_THEME
import org.koin.android.viewmodel.ext.android.viewModel

class ThemeDialogFragment : BaseBottomSheetDialogFragment() {

    private val binding by lazy { FragmentDialogThemeBinding.inflate(layoutInflater) }

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.theme.observe(viewLifecycleOwner, Observer { theme ->

            when (theme ?: FOLLOW_SYSTEM_THEME) {
                FOLLOW_SYSTEM_THEME -> binding.rbFollowSystemTheme.isChecked = true
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

        binding.rbFollowSystemTheme.setOnClickListener {
            dismiss()
            viewModel.setThemeValue(FOLLOW_SYSTEM_THEME)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        return binding.root
    }

}