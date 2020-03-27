package com.apps.chatychaty.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apps.chatychaty.DURATION
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.FragmentProfileBinding
import com.apps.chatychaty.getPref
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.ProfileViewModel
import com.apps.chatychaty.viewModel.ProfileViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), Error, UpdateName {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(Repos.userRepository)
    }

    private val args by navArgs<ProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.error = this
        viewModel.updateName = this

        binding.tb.setNavigationOnClickListener {

            enterTransition =
                MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true).apply {
                    duration = DURATION
                }

            this.findNavController().navigateUp()
        }
        binding.tb.navigationIcon?.setTint(resources.getColor(R.color.colorOnPrimary_900))


        binding.name.setText(args.name)
        val username = "@${args.username}"
        binding.username.text = username
        Glide.with(this).load(args.imgUrl)
            .circleCrop()
            .apply(RequestOptions.overrideOf(125, 125))
            .into(binding.img)


        val menuEditItem = binding.tb.menu.findItem(R.id.edit)

        val menuDoneItem = binding.tb.menu.findItem(R.id.done)

        if (args.chatId != 0){
            menuEditItem.isVisible = false
            binding.cl.isVisible = false
        }

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

//            binding.img.let { img ->
//                img.setOnClickListener {
//
//                    val intent = Intent(Intent.ACTION_PICK).apply {
//                        this.type = "image/*"
//                        this.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
//                    }
//                    if (intent.resolveActivity(activity?.packageManager!!) != null) {
//                        startActivityForResult(intent, 1)
//                    }
//                }
//
//            }


            true
        }

        menuDoneItem.setOnMenuItemClickListener {
            it.isVisible = false
            menuEditItem.isVisible = true

            binding.name.isEnabled = false

            imm.hideSoftInputFromWindow(binding.name.windowToken, 0)

            viewModel.updateName(binding.name.text.toString())
//            viewModel.updatePhoto()


            true
        }


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                viewModel.imgPath = data?.data?.path!!

//                binding.img.setImageURI(img)
//                val array = arrayOf(MediaStore.Images.Media.DATE_ADDED)
//                val cursor =
//                    activity?.contentResolver?.query(img ?: Uri.EMPTY, array, null, null, null)
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

    override fun snackbar(value: String) {
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }

    override fun updateName(name: String) {
        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
            this.putString("name", name)
        }
    }
}

internal interface UpdateName {
    fun updateName(name: String)
}
