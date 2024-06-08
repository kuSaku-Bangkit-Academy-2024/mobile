package com.capstone.kusaku.ui.register

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
import com.capstone.kusaku.R
import com.capstone.kusaku.databinding.ActivityRegisterBinding
import com.capstone.kusaku.ui.ViewModelFactory
import com.capstone.kusaku.ui.login.LoginActivity
import com.capstone.kusaku.utils.ProgressBarHelper
import com.capstone.kusaku.utils.Status

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressBarHelper: ProgressBarHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBarHelper = ProgressBarHelper(this)

        setupSignInText()
        onSignUp()
    }

    private fun onSignUp() {
        binding.apply {
            btnRegister.setOnClickListener {
                val email = edtEmailRegister.text.toString()
                val password = edtPasswordRegister.text.toString()
                val confirmPassword = edtConfirmationPasswordRegister.text.toString()
                val income = edtMonthlyIncomeRegister.text.toString()
                val name = edtNameRegister.text.toString()

                if (validateInput(email, password, confirmPassword, income, name)) {
                    viewModel.register(email, password, confirmPassword, income, name).observe(this@RegisterActivity){
                        when (it.status){
                            Status.SUCCESS -> {
                                progressBarHelper.hide()
                                Toast.makeText(this@RegisterActivity, "Successfully registered", Toast.LENGTH_SHORT).show()
                                moveActivity()
                            }
                            Status.ERROR -> {
                                progressBarHelper.hide()
                                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                            }
                            Status.LOADING -> progressBarHelper.show()
                        }
                    }
                }
            }
        }
    }

    private fun moveActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInput(
        email: String,
        password: String,
        confirmPassword: String,
        income: String,
        name: String
    ): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.edtEmailRegister.error = getString(R.string.required_field)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.edtPasswordRegister.error = getString(R.string.required_field)
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.edtConfirmationPasswordRegister.error = getString(R.string.required_field)
            isValid = false
        }

        if (income.isEmpty()) {
            binding.edtMonthlyIncomeRegister.error = getString(R.string.required_field)
            isValid = false
        }

        if (name.isEmpty()) {
            binding.edtNameRegister.error = getString(R.string.required_field)
            isValid = false
        }

        if (password != confirmPassword) {
            binding.edtConfirmationPasswordRegister.error = getString(R.string.passwords_dont_match)
            isValid = false
        }

        return isValid
    }


    private fun setupSignInText() {
        val tvSignIn: TextView = binding.tvToSignIn
        val text = getString(R.string.got_an_account)

        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val greenColor = ContextCompat.getColor(this, R.color.teal_700)
        val greenColorSpan = ForegroundColorSpan(greenColor)

        val startIndex = text.indexOf("Sign In")
        val endIndex = startIndex + "Sign In".length

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(greenColorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvSignIn.text = spannableString
        tvSignIn.movementMethod = LinkMovementMethod.getInstance()
    }
}