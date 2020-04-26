package com.apps.chatychaty.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.FragmentProfileBinding
import com.apps.chatychaty.local.AppDatabase
import com.apps.chatychaty.util.getPref
import com.apps.chatychaty.util.setPref
import com.apps.chatychaty.viewModel.ProfileViewModel
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = this
        }
    }

    private val viewModel by viewModel<ProfileViewModel>()

    private val args by navArgs<ProfileFragmentArgs>()

    private val imm by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        enterTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.Y, true).apply {
                duration = DURATION
            }

        exitTransition =
            MaterialSharedAxis.create(MaterialSharedAxis.Y, false).apply {
                duration = DURATION
            }

        binding.tb.setNavigationOnClickListener {

            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)

            this.findNavController().navigateUp()
        }
        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorPrimary))

        binding.name.setText(args.name)
        val username = "@${args.username}"
        binding.username.text = username


        val menuEditItem = binding.tb.menu.findItem(R.id.edit)

        val menuDoneItem = binding.tb.menu.findItem(R.id.done)

        if (args.chatId != 0) {
            menuEditItem.isVisible = false
            binding.cl.isVisible = false
        }

        menuEditItem.icon.setTint(resources.getColor(R.color.colorPrimary))

        menuDoneItem.icon.setTint(resources.getColor(R.color.colorPrimary))

        val enterAnimation = MaterialFade.create(true).apply {
            duration = DURATION
        }

        val exitAnimation = MaterialFade.create(false).apply {
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)
                            val intent = Intent(
                                Intent.ACTION_GET_CONTENT,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            ).apply {
                                this.type = "image/*"
                            }
//                        this.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
//                        val intent = Intent(Intent.ACTION_PICK)
//                        intent.type = "image/*"
                            if (intent.resolveActivity(activity?.packageManager!!) != null) {
                                startActivityForResult(intent, 1)
                            }
                        } else {
                            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG)
                                .show()
                        }
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
//                val uri = data?.data
//                if (uri != null) {

//                    Timber.i(uri.toString())
//                    Timber.i(uri.path.toString())
//                val path = data?.data.path
//                val file = File(path ?: "")
//                viewModel.updatePhoto(file)

//                val selectedImage = data?.data!!
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                // Get the cursor
                // Get the cursor
//                val cursor =
//                    activity?.contentResolver?.query(
//                        selectedImage,
//                        filePathColumn,
//                        null,
//                        null,
//                        null
//                    )
                // Move to first row
                // Move to first row
//                cursor.moveToFirst()
                //Get the column index of MediaStore.Images.Media.DATA
                //Get the column index of MediaStore.Images.Media.DATA
//                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                //Gets the String value in the column
                //Gets the String value in the column
//                val imgDecodableString: String = cursor.getString(columnIndex)
//                cursor?.close()


//            }
                // Set the Image in ImageView after decoding the String
                // Set the Image in ImageView after decoding the String
//                imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString))

//                    val file = File(URI.create(uri.path ?: ""))
//                        viewModel.updatePhoto(file)

//                    viewModel.updatePhoto(file)
//                    Timber.i(file.toString())

//                    binding.img.setImageURI(uri)

//                val inputStream = activity?.contentResolver?.openInputStream(uri)

//                inputStream?.buffered()?.use {
//                    val imageData: ByteArray = it.readBytes()
////                        viewModel.updatePhoto(imageData)
//                }
//            }

//                val path = uri?.path

//                val file = File(path ?: "")
//                viewModel.updatePhoto(file)

//                binding.img.setImageURI(uri)
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
}