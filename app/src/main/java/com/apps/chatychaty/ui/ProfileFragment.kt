package com.apps.chatychaty.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.database.AppDatabase
import com.apps.chatychaty.databinding.FragmentProfileBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.util.getPref
import com.apps.chatychaty.util.setPref
import com.apps.chatychaty.util.snackbar
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.ProfileViewModel
import com.apps.chatychaty.viewModel.ProfileViewModelFactory
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), Error, UpdateName {

    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
        }
    }

    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(Repos.userRepository)
    }

    private val args by navArgs<ProfileFragmentArgs>()

    private val imm by lazy {
        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.error = this

        viewModel.updateName = this

        enterTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, true).apply {
                duration = DURATION
            }

        exitTransition =
            MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Y, false).apply {
                duration = DURATION
            }

        binding.tb.setNavigationOnClickListener {

            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)

            this.findNavController().navigateUp()
        }
        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorOnPrimary_900))

        binding.name.setText(args.name)
        val username = "@${args.username}"
        binding.username.text = username


        val menuEditItem = binding.tb.menu.findItem(R.id.edit)

        val menuDoneItem = binding.tb.menu.findItem(R.id.done)

        if (args.chatId != 0) {
            menuEditItem.isVisible = false
            binding.cl.isVisible = false
        }

        menuEditItem.icon.setTint(resources.getColor(R.color.colorOnPrimary_900))

        menuDoneItem.icon.setTint(resources.getColor(R.color.colorOnPrimary_900))

        val enterAnimation = MaterialFade.create(requireContext(), true).apply {
            duration = DURATION
        }

        val exitAnimation = MaterialFade.create(requireContext(), false).apply {
            duration = DURATION
        }

        menuEditItem.setOnMenuItemClickListener {

            TransitionManager.beginDelayedTransition(binding.tb, exitAnimation)

            it.isVisible = false

            menuDoneItem.isVisible = true

            binding.name.isEnabled = true

            binding.name.requestFocus()

            binding.name.setSelection(binding.name.text.length)

            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )

            binding.img.let { img ->
                img.setOnClickListener {
                    imm.hideSoftInputFromWindow(binding.name.windowToken, 0)
                    val intent = Intent(Intent.ACTION_PICK).apply {
                        this.type = "image/*"
                        this.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                    }
                    if (intent.resolveActivity(activity?.packageManager!!) != null) {
                        startActivityForResult(intent, 1)
                    }
                }

            }


            true
        }





        menuDoneItem.setOnMenuItemClickListener {
            TransitionManager.beginDelayedTransition(binding.tb, enterAnimation)

            it.isVisible = false

            menuEditItem.isVisible = true

            binding.name.isEnabled = false

            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)

            viewModel.updateName(binding.name.text.toString())
//            viewModel.updatePhoto()


            true
        }

        binding.btnSignOut.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)
            this.findNavController()
                .navigate(ProfileFragmentDirections.actionProfileFragmentToSignGraph())

            signOut()
        }


        val followSystemTheme = resources.getString(R.string.follow_system_theme)
        val lightTheme = resources.getString(R.string.light_theme)
        val darkTheme = resources.getString(R.string.dark_theme)

        binding.tvAppThemeOption.text = resources.getString(R.string.follow_system_theme)

        when (activity?.getPref("theme")) {
            followSystemTheme ->
                binding.tvAppThemeOption.text = resources.getString(R.string.follow_system_theme)

            lightTheme ->
                binding.tvAppThemeOption.text = resources.getString(R.string.light_theme)

            darkTheme ->
                binding.tvAppThemeOption.text = resources.getString(R.string.dark_theme)

        }

        binding.tvAppThemeOption.setOnClickListener {
            themeDialog()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                val img = data?.data
//
//        Timber.i("${img?.path}")
//                binding.img.setImageURI(img)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    activity?.contentResolver?.query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        arrayOf(MediaStore.Images.Media._ID),
//                        null,
//                        null
//                    )?.use { cursor ->
//                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//
//                        while (cursor.moveToNext()) {
//                            val id = cursor.getLong(idColumn)
//
//                            val contentUri = ContentUris.withAppendedId(
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                id
//                            )
//                        }
//
//                    }
//                }
//                cursor?.moveToFirst()
//                val columnIndex = cursor?.getColumnIndex(array[0])
//                val imgString = cursor?.getString(columnIndex!!)
//                cursor?.close()

//                viewModel.imgPath = img?.path ?: ""
//               val file = File(img?.lastPathSegment!!)
//                Timber.i(file.absolutePath)
//                Timber.i(img.lastPathSegment!!)
//                Timber.i(URI(img!!).toString())

            }
        } else {
            snackbar("Error occurred!")
        }
    }

    private fun signOut() {

        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(requireContext()).clearAllTables()

        }
        activity?.getPreferences(Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
    }

    private fun themeDialog() {
        AlertDialog.Builder(context)
            .setTitle(resources.getString(R.string.app_theme))
            .setItems(
                arrayOf(
                    resources.getString(R.string.follow_system_theme),
                    resources.getString(R.string.light_theme),
                    resources.getString(R.string.dark_theme)
                )
            ) { dialog, which ->
                when (which) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                        activity?.setPref(
                            "theme",
                            resources.getString(R.string.follow_system_theme)
                        )

                        binding.tvAppThemeOption.text =
                            resources.getString(R.string.follow_system_theme)

                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        activity?.setPref(
                            "theme",
                            resources.getString(R.string.light_theme)
                        )


                        binding.tvAppThemeOption.text =
                            resources.getString(R.string.light_theme)
                    }
                    2 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        activity?.setPref(
                            "theme",
                            resources.getString(R.string.dark_theme)
                        )

                        binding.tvAppThemeOption.text = resources.getString(R.string.dark_theme)
                    }
                }
            }.create().show()
    }

    override fun snackbar(value: String) {
        binding.cool.snackbar(value)
    }

    override fun updateName(name: String) {
        activity?.setPref("name", name)
    }
}

internal interface UpdateName {
    fun updateName(name: String)
}
