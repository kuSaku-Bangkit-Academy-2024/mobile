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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.kusaku.R
import com.capstone.kusaku.databinding.ActivityLoginBinding
import com.capstone.kusaku.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSignUpText()
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (edtEmailLogin.length() == 0 || edtPasswordLogin.length() == 0) {
                    edtEmailLogin.error = getString(R.string.required_field)
                    edtPasswordLogin.error = getString(R.string.required_field)
                } else if (edtEmailLogin.length() != 0 && edtPasswordLogin.length() != 0) {
                    /* move with data, next task
                    postText()
                    showToast()
                    loginViewModel.login()
                    moveActivity()
                     */
                }
            }
        }
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