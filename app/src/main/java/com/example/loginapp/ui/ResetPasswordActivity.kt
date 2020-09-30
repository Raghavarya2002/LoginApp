package com.example.loginapp.ui

import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapp.R
import com.example.loginapp.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.text_email

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        button_reset_password.setOnClickListener {
            val email = text_email.text.toString().trim()

            if(email.isEmpty()){
                text_email.error="Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                text_email.error="Valid Email Required"
                text_email.requestFocus()
                return@setOnClickListener

            }
          progressBar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener {task ->
                    progressBar.visibility = View.INVISIBLE
                    if(task.isSuccessful){
                        this.toast("Email Sent Successfully")
                    }else{
                        this.toast(task?.exception?.message!!)
                    }

                }
        }


        }
    }
