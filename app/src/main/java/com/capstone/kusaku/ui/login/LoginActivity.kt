package com.capstone.kusaku.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.capstone.kusaku.R
import com.capstone.kusaku.data.remote.response.LoginResponse
import com.capstone.kusaku.databinding.ActivityLoginBinding
import com.capstone.kusaku.ui.ViewModelFactory
import com.capstone.kusaku.ui.main.MainActivity
import com.capstone.kusaku.ui.register.RegisterActivity
import com.capstone.kusaku.utils.ProgressBarHelper
import com.capstone.kusaku.utils.Resource
import com.capstone.kusaku.utils.Status

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBarHelper: ProgressBarHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBarHelper = ProgressBarHelper(this)

        setupSignUpText()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edtEmailLogin.text.toString()
                val password = edtPasswordLogin.text.toString()
                if (validateInput(email, password)) {
                    observeLogin(viewModel.login(email, password))
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.edtEmailLogin.error = getString(R.string.required_field)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.edtPasswordLogin.error = getString(R.string.required_field)
            isValid = false
        }

        return isValid
    }

    private fun observeLogin(liveData:  LiveData<Resource<LoginResponse>>) {
        liveData.observe(this@LoginActivity) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    progressBarHelper.hide()
                    Toast.makeText(this@LoginActivity, "Success login", Toast.LENGTH_SHORT).show()
                    moveActivity()
                }
                Status.ERROR -> {
                    progressBarHelper.hide()
                    Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    progressBarHelper.show()
                }
            }
        }
    }

    private fun moveActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() to close the LoginActivity
    }

    private fun setupSignUpText() {
        val tvSignUp: TextView = findViewById(R.id.tv_to_sign_up)
        val text = "Don't have an account? Sign Up"

        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        val greenColor = ContextCompat.getColor(this, R.color.teal_700)
        val greenColorSpan = ForegroundColorSpan(greenColor)

        val startIndex = text.indexOf("Sign Up")
        val endIndex = startIndex + "Sign Up".length

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(greenColorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvSignUp.text = spannableString
        tvSignUp.movementMethod = LinkMovementMethod.getInstance()
    }
}