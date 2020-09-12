package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register.setOnClickListener {
            val email = edit_text_email.text.toString().trim()
            val password =edit_text_password.text.toString().trim()
            if(email.isEmpty()){
                edit_text_email.error="Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.error="Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener

            }
            if(password.isEmpty() || password.length < 6)
            {
                edit_text_password.error="At least 6 characters are required"
                edit_text_password.requestFocus()
                return@setOnClickListener

            }
        }

        text_view_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }
}