package com.example.socialapp.presentation.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.socialapp.MainActivity
import com.example.socialapp.databinding.ActivitySignUpBinding
import com.example.socialapp.utils.Prefs
import com.example.socialapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sign_up.*

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val viewModel by viewModels <AuthenticationViewModel>()
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = Prefs(this)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btn_signUp.setOnClickListener {
            val name = ed_name_sign_up.text.toString()
            val lastName = ed_last_name_sign_up.text.toString()
            val email = ed_email_sign_up.text.toString()
            val password = ed_password_sign_up.text.toString()
            if (email.isNotEmpty() && name.isNotEmpty() && lastName.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launchWhenStarted {
                    viewModel.signUp(email, password, name, lastName)
                    prefs.save("state",1)
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please enter all the data", Toast.LENGTH_LONG).show()
                prefs.save("state",0)
            }
            when (val resource = viewModel.signUpState.value) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    if (resource.data) {
                    } else {
                        Toast.makeText(this, "Sign up failed", Toast.LENGTH_LONG).show()
                        prefs.save("state",0)
                    }
                }
                is Response.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    prefs.save("state", 0)
                }

            }
        }

    }
    fun backToSignIn (view: View){
        val intent = Intent(this@SignUpActivity,LoginActivity::class.java)
        startActivity(intent)
    }

}