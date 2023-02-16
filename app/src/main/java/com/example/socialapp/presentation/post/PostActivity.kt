package com.example.socialapp.presentation.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.socialapp.MainActivity
import com.example.socialapp.databinding.ActivityPostBinding
import com.example.socialapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_post.*


@AndroidEntryPoint
class PostActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()
    lateinit var binding: ActivityPostBinding
    private var imageUri: Uri? = null
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btn_save.setOnClickListener {
            val text = ed_post.text.toString()
            viewModel.getUser()
            lifecycleScope.launchWhenStarted {
                viewModel.getUserState.collect {
                    when (it) {
                        is Response.Success -> {
                            Log.e("PostActivity", "Success")
                            viewModel.setPostData(imageUri.toString(), it.data, text, null, null, ArrayList(), null, ArrayList(), null)
                        }
                        is Response.Loading -> {
                            Log.e("PostActivity", "Loading")
                        }
                        is Response.Error -> {
                            Log.e("PostActivity", it.message)
                        }
                    }
                }
            }
          val intent = Intent(this@PostActivity, MainActivity::class.java)
        startActivity(intent)
    }

im_btn_add.setOnClickListener {

    Intent(Intent.ACTION_GET_CONTENT).also {
        it.type = "image/*"
        resultLauncher.launch(it)
    }
}
    resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            viewModel.uploadImage(imageUri!!)
            binding.imBtnAdd.setImageURI(imageUri)
            binding.imBtnAdd.scaleType=ImageView.ScaleType.CENTER_CROP
        }
    }
}
}