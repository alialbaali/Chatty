package com.apps.chatychaty.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apps.chatychaty.R
import com.apps.chatychaty.databinding.FragmentSignUpBinding
import com.apps.chatychaty.network.Repos
import com.apps.chatychaty.viewModel.Error
import com.apps.chatychaty.viewModel.Sign
import com.apps.chatychaty.viewModel.SignSharedViewModel
import com.apps.chatychaty.viewModel.SignSharedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment(), Sign, Error {

    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by viewModels<SignSharedViewModel> {
        SignSharedViewModelFactory(Repos.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        exitTransition =  MaterialFadeThrough.create(requireContext()).apply {
            duration = 500
        }

        enterTransition =  MaterialFadeThrough.create(requireContext()).apply {
            duration = 500
        }

        binding.let {

            it.lifecycleOwner = this
            it.viewModel = viewModel

        }

        binding.tvSignIn.setOnClickListener {
            this.findNavController()
                .navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isBlank()) {
                binding.tilName.error = resources.getString(R.string.name_error)
            }
            if (username.isBlank()) {
                binding.tilUsername.error = resources.getString(R.string.username_error)
            }
            if (password.isBlank()) {
                binding.tilPassword.error = resources.getString(R.string.password_error)
            }

            if (name.isNotBlank() and username.isNotBlank() and password.isNotBlank()) {
                viewModel.signUp()
            }
        }

        viewModel.let { viewModel ->

            viewModel.error = this
            viewModel.sign = this

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
        Snackbar.make(binding.cool, value, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
    }

    override fun putPreferences(token: String, name: String, username: String, imgUrl: String) {
        this.findNavController().navigate(SignUpFragmentDirections.actionGlobalListFragment())

        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
            this.putString("token", token)
            this.putString("name", name)
            this.putString("username", username)
            this.putString("img_url", imgUrl)
            apply()
        }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 1) {
//                val img = data?.data
//                img?.path
////                binding.img.setImageURI(img)
////                val array = arrayOf(MediaStore.Images.Media.DATE_ADDED)
////                val cursor =
////                    activity?.contentResolver?.query(img ?: Uri.EMPTY, array, null, null, null)
////                cursor?.moveToFirst()
////                val columnIndex = cursor?.getColumnIndex(array[0])
////                val imgString = cursor?.getString(columnIndex!!)
////                cursor?.close()
//
//                viewModel.img = img?.path ?: ""
//                snackbar(img?.path ?: "")
//
//            }
//        } else {
//            snackbar("Error occurred!")
//        }
//    }
}
