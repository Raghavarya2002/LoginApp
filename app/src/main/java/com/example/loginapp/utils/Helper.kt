package com.example.loginapp.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.loginapp.ui.HomeActivity
import com.example.loginapp.ui.LoginActivity

fun Context.toast(message: String) =
    Toast.makeText(this, message , Toast.LENGTH_SHORT).show()
fun Context.login(){
    val intent = Intent(this, HomeActivity::class.java)
    startActivity(intent)
}

fun Context.logout(){
    val intent = Intent(this, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}