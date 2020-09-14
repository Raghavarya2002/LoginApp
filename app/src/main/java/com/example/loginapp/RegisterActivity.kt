package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mauth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mauth = FirebaseAuth.getInstance()


        button_register.setOnClickListener {
            val email = edit_text_email.text.toString().trim()
            val password = edit_text_password.text.toString().trim()
            if (email.isEmpty()) {
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edit_text_email.error = "Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener

            }
            if (password.isEmpty() || password.length < 6) {
                edit_text_password.error = "At least 6 characters are required"
                edit_text_password.requestFocus()
                return@setOnClickListener

            }

            registeruser(email, password)
        }

        text_view_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registeruser(email: String, password: String) {
        progressbar.visibility = View.VISIBLE
        mauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.INVISIBLE
                if (task.isSuccessful) {
                    //Registration Successfull
                    login()
                    startActivity(intent)
                } else {
                    task.exception?.message?.let {
                        toast(it)
                    }


                }

            }

    }

    override fun onStart() {
        super.onStart()
        mauth.currentUser?.let {
            login()
        }
    }
}