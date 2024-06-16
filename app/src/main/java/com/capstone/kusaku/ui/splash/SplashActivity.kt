package com.capstone.kusaku.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kusaku.databinding.ActivitySplashBinding
import com.capstone.kusaku.ui.ViewModelFactory
import com.capstone.kusaku.ui.login.LoginActivity
import com.capstone.kusaku.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgSplashLogo.animate()
            .setDuration(SPLASH_DELAY)
            .alpha(1f)
            .withEndAction {
                navigateToNextScreen()
            }
    }

    private fun navigateToNextScreen() {
        viewModel.getUserSession().observe(this@SplashActivity){
            if (it.token.isNullOrEmpty()){
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        binding.imgSplashLogo.clearAnimation()
        super.onDestroy()
    }

    companion object {
        private const val SPLASH_DELAY: Long = 2500
    }
}
