package com.apps.chatychaty.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.databinding.FragmentSignUpBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.LogIn
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment(), LogIn, Error {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by viewModels<SignSharedViewModel> {
        SignSharedViewModelFactory(Repos.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.let {

            it.lifecycleOwner = this
            it.viewModel = viewModel

        }

        binding.tvSignIn.let { tvSignIn ->
            tvSignIn.setOnClickListener {
                this.findNavController()
                    .navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }

//        binding.btnSignUp.let { btnSignUp ->
//
//            btnSignUp.setOnClickListener {
//                viewModel.createAccount()
//            }
//
//        }

        viewModel.let { viewModel ->

            viewModel.error = this
            viewModel.logIn = this

        }

//        binding.img.let { img ->
//            img.setOnClickListener {
//
//                val intent = Intent(Intent.ACTION_PICK).apply {
//                    this.type = "image/*"
//                    this.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
//                }
//                if (intent.resolveActivity(activity?.packageManager!!) != null) {
//                    startActivityForResult(intent, 1)
//                }
//            }
//
//        }


        return binding.root
    }

    override fun snackbar(value: String) {
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG).show()
    }

    override fun putPreferences(username: String, token: String?) {

        this.findNavController().navigate(SignUpFragmentDirections.actionGlobalChatFragment())

        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
            this.putString("username", username)
            this.putString("token", token)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                val img = data?.data
                img?.path
//                binding.img.setImageURI(img)
//                val array = arrayOf(MediaStore.Images.Media.DATE_ADDED)
//                val cursor =
//                    activity?.contentResolver?.query(img ?: Uri.EMPTY, array, null, null, null)
//                cursor?.moveToFirst()
//                val columnIndex = cursor?.getColumnIndex(array[0])
//                val imgString = cursor?.getString(columnIndex!!)
//                cursor?.close()

                viewModel.img = img?.path ?: ""
                snackbar(img?.path ?: "")

            }
        } else {
            snackbar("Error occurred!")
        }
    }
}
