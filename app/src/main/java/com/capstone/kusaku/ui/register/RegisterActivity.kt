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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.kusaku.R
import com.capstone.kusaku.databinding.ActivityRegisterBinding
import com.capstone.kusaku.ui.login.LoginActivity
import com.capstone.kusaku.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSignInText()
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }
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