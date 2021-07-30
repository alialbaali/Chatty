package com.chatychaty.app.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.chatychaty.app.databinding.FragmentDialogThemeBinding
import com.chatychaty.app.util.BaseBottomSheetDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.domain.model.Theme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThemeDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogThemeBinding

    private val viewModel by viewModel<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentDialogThemeBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
        }

        viewModel.theme
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {

                    }
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        when (state.value) {
                            Theme.SYSTEM -> binding.rbSystemDefault.isChecked = true
                            Theme.LIGHT -> binding.rbLightTheme.isChecked = true
                            Theme.DARK -> binding.rbDarkTheme.isChecked = true
                        }
                    }
                }
            }.launchIn(lifecycleScope)

        binding.rbLightTheme.setOnClickListener {
            dismiss()
            viewModel.updateTheme(Theme.LIGHT)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.rbDarkTheme.setOnClickListener {
            dismiss()
            viewModel.updateTheme(Theme.DARK)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.rbSystemDefault.setOnClickListener {
            dismiss()
            viewModel.updateTheme(Theme.SYSTEM)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        return binding.root
    }

}