package com.chatychaty.app.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.chatychaty.app.BaseBottomSheetDialogFragment
import com.chatychaty.app.ConfirmationDialogFragment
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentDialogInfoEditBinding
import com.chatychaty.app.util.snackbar
import com.chatychaty.app.util.toast
import org.koin.android.viewmodel.ext.android.viewModel

private const val PERMISSION_REQUEST_CODE = 1
private const val ACTIVITY_REQUEST_CODE = 2

class EditInfoDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogInfoEditBinding

    private val viewModel by viewModel<ProfileViewModel>()

    private val imm by lazy { requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentDialogInfoEditBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@EditInfoDialogFragment
            viewModel = this@EditInfoDialogFragment.viewModel
        }

        viewModel.getUser()

        binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorBackground))

        binding.fab.setOnClickListener {

            imm.hideSoftInputFromWindow(binding.tilName.windowToken, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

                if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    chooseImage()
                else
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            else
                chooseImage()

        }

        setUpConfirmBtnOnClickListener()

        return binding.root
    }

    private fun chooseImage() {

        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null)
            startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
        else
            showAppRequiredDialog()


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == PERMISSION_REQUEST_CODE)
            chooseImage()
        else

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                showPermissionRequiredDialog()
            else
                binding.root.toast(resources.getString(R.string.permission_not_granted))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_REQUEST_CODE) {

            try {

                val uri = intent?.data ?: throw Exception()

                Glide.with(requireContext())
                    .load(uri)
                    .placeholder(R.drawable.ic_person_24dp)
                    .circleCrop()
                    .into(binding.iv)

                val contentResolver = requireActivity().contentResolver

                val cursor = contentResolver.query(uri, null, null, null, null)

                val filename: String

                if (cursor != null) {

                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                    cursor.moveToFirst()

                    filename = cursor.getString(columnIndex)

                    cursor.close()

                } else {

                    filename = "image"

                }

                val inputStream = contentResolver.openInputStream(uri)

                inputStream?.buffered()?.use {
                    setUpConfirmBtnOnClickListener(it.readBytes(), filename)
                }

            } catch (e: Exception) {
                binding.root.snackbar(e.message ?: "Error happened")
            }
        }
    }

    private fun showPermissionRequiredDialog() {

        ConfirmationDialogFragment { dialogBinding, dialogFragment ->

            dialogBinding.tvTitle.text = getString(R.string.permission_required)

            dialogBinding.tvSubtitle.isAllCaps = false

            dialogBinding.tvSubtitle.text = getString(R.string.permission_required_desc)

            dialogBinding.btnConfirm.text = getString(R.string.grant_permission)

            dialogBinding.btnConfirm.setOnClickListener {
                dialogFragment.dismiss()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            }

        }.show(parentFragmentManager, null)

    }

    private fun showAppRequiredDialog() {
        ConfirmationDialogFragment { dialogBinding, dialogFragment ->

            dialogBinding.tvTitle.text = getString(R.string.app_required)

            dialogBinding.tvSubtitle.isAllCaps = false

            dialogBinding.tvSubtitle.text = getString(R.string.app_required_desc)

            dialogBinding.btnConfirm.text = getString(R.string.ok)

            dialogBinding.btnConfirm.setOnClickListener {
                dialogFragment.dismiss()
            }

        }.show(parentFragmentManager, null)
    }

    private fun setUpConfirmBtnOnClickListener(byteArray: ByteArray? = null, filename: String? = null) {

        binding.btnConfirm.setOnClickListener {

            if (byteArray != null && filename != null) viewModel.updatePhoto(byteArray, filename)

            if (binding.etName.text.isNullOrBlank()) {

                binding.tilName.error = resources.getString(R.string.name_empty)

            } else {

                dismiss()

                viewModel.updateName(binding.etName.text.toString())

            }
        }

    }
}