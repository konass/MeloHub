package com.example.socialapp.presentation.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentProfileEditBinding
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_edit.*

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {
    lateinit var binding : FragmentProfileEditBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private var imageUri: Uri?= null
    private val bundleArgs by navArgs<ProfileEditFragmentArgs>()
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentProfileEditBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ed_name_profile.text=bundleArgs.currentUser.name.toEditable()
        binding.edLastNameProfile.setText(bundleArgs.currentUser.lastName)
        binding.edDesc.setText(bundleArgs.currentUser.description)
        binding.save.setOnClickListener {
        val name = ed_name_profile.text.toString()
        val lastName = ed_last_name_profile.text.toString()
        val description = ed_desc.text.toString()
            lifecycleScope.launchWhenStarted {
                viewModel.updateUser(name = name,
                    lastName = lastName,
                    description = description,
                    imageUrl = imageUri.toString())
                when (val resource = viewModel.updateUserState.value) {
                    is Response.Success -> {
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error", Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading")
                    }
                }
            }
            view.findNavController().navigate(
                R.id.action_profileEditFragment_to_profileFragment
            )
        }
        binding.imAvatar.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                resultLauncher.launch(it)
            }
        }
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                viewModel.uploadUserImage(imageUri!!)
                Glide.with(requireContext()).load(imageUri).into(im_avatar)
                binding.imAvatar.scaleType= ImageView.ScaleType.CENTER_CROP
            }
        }
        }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
    }

