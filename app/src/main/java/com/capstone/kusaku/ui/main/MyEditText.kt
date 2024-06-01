package com.capstone.kusaku.ui.main

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.capstone.kusaku.R
import com.google.android.material.textfield.TextInputEditText

class MyEditText : TextInputEditText {
    private var isError: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateInput(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                validateInput(p0.toString())
            }
        })
    }

    private fun validateInput(input: String) {
        when (inputType) {
            EMAIL -> {
                isError = if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    error = context.getString(R.string.email_validation)
                    true
                } else {
                    false
                }
            }
            PASSWORD -> {
                isError = if (input.length < 8) {
                    error = context.getString(R.string.password_length)
                    true
                } else {
                    false
                }
            }
        }
    }

    companion object {
        const val EMAIL = 0x00000021
        const val PASSWORD = 0x00000081
    }
}