package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edit_text_email
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mauth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mauth = FirebaseAuth.getInstance()
        button_sign_in.setOnClickListener {
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
            
            loginuser(email,password)

        }

        text_view_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
    }

    private fun loginuser(email: String, password: String) {
        progressbar1.visibility = View.VISIBLE
        mauth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                progressbar1.visibility = View.INVISIBLE
                if(task.isSuccessful){
                    login()


                }
                else{
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