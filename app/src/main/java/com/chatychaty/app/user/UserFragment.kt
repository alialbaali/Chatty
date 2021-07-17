package com.chatychaty.app.user

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chatychaty.app.R
import com.chatychaty.app.databinding.FragmentUserBinding
import com.chatychaty.app.util.PermissionDialogFragment
import com.chatychaty.app.util.UiState
import com.chatychaty.app.util.snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val JPEG = "image/jpeg"
private const val PNG = "image/png"

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    private val viewModel by viewModel<UserViewModel>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var storageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                selectImage()
            else
                binding.root.snackbar(getString(R.string.permission_not_granted))
        }
        storageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.data?.let { uri ->
                uploadImage(uri)
                findNavController().navigate(UserFragmentDirections.actionUserFragmentToProgressDialogFragment())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentUserBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        collectState()
        setupListeners()

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(JPEG, PNG))
        }
        storageLauncher.launch(intent)
    }

    private fun showPermissionRequiredDialog() {
        PermissionDialogFragment()
            .show(parentFragmentManager, null)
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    @SuppressLint("SetTextI18n")
    private fun collectState() {
        viewModel.user
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {

                    }
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        val chat = state.value
                        binding.tvName.text = chat.name
                        binding.tvUsername.text = "@${chat.username}"
                        binding.imageUrl = chat.imageUrl
                    }
                }
            }
            .launchIn(lifecycleScope)

        viewModel.state
            .onEach { state ->
                when (state) {
                    is UiState.Failure -> {
                        findNavController().navigateUp()
                        binding.root.snackbar("${state.exception.message}")
                    }
                    is UiState.Loading -> {
                    }
                    is UiState.Success -> {
                        findNavController().navigateUp()
                        binding.root.snackbar("Image has been updated successfully")
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        binding.tb.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvChangeName.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToNameDialogFragment())
        }

        binding.tvChangePassword.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToPasswordDialogFragment())
        }

        binding.tvSubmitFeedback.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToFeedbackDialogFragment())
        }

        binding.tvSignOut.setOnClickListener {
            signOut()
        }

        binding.tvDeleteAccount.setOnClickListener {
            signOut()
        }
        binding.fab.setOnClickListener {
            when {
                requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> selectImage()
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showPermissionRequiredDialog()
                else -> requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun uploadImage(uri: Uri) {
        try {

            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(binding.ivImage)

            val contentResolver = requireActivity().contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            val filename: String = if (cursor != null) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(columnIndex).also {
                    cursor.close()
                }
            } else {
                "image"
            }

            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.buffered()?.use {
                val byteArray = it.readBytes()
                viewModel.updateImage(byteArray, filename)
            }
        } catch (e: Exception) {
            binding.root.snackbar(e.message ?: "Error happened")
        }
    }

    private fun signOut() {
        findNavController().navigate(UserFragmentDirections.actionUserFragmentToSignGraph())
        viewModel.signOut()
    }

}