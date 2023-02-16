package com.example.socialapp.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.socialapp.MainActivity
import com.example.socialapp.R
import com.example.socialapp.presentation.authentication.AuthenticationViewModel
import com.example.socialapp.presentation.authentication.LoginActivity
import com.example.socialapp.utils.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthenticationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs =Prefs(this)
        setContentView(R.layout.activity_splash)
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            if (prefs.getValueInt("state")==0) {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
            } else if (prefs.getValueInt("state")==1){
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}