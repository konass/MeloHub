package com.example.socialapp.presentation.authentication


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.socialapp.MainActivity
import com.example.socialapp.databinding.ActivityLoginBinding
import com.example.socialapp.utils.Prefs
import com.example.socialapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels <AuthenticationViewModel>()
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = Prefs(this)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btn_signIn.setOnClickListener{
            val email = ed_email.text.toString()
            val password = ed_password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launchWhenStarted {
                    viewModel.signIn(email, password)
                    prefs.save("state", 1)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please enter all the data", Toast.LENGTH_LONG).show()
                prefs.save("state", 0)
            }
            when (val resource = viewModel.signInState.value) {
                is Response.Loading -> {
                }
                is Response.Success -> {
                    if (resource.data) {
                    } else {
                        Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show()
                        prefs.save("state", 0)
                    }
                }
                is Response.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    prefs.save("state", 0)
                }

            }
        }
    }
    fun backToSignUp(view: View){
        val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
        startActivity(intent)
    }
}