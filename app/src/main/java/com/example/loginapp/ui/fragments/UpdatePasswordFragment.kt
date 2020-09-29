package com.example.loginapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.loginapp.R
import com.example.loginapp.utils.toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.fragment_update_email.*
import kotlinx.android.synthetic.main.fragment_update_email.button_authenticate
import kotlinx.android.synthetic.main.fragment_update_email.edit_text_password
import kotlinx.android.synthetic.main.fragment_update_email.layoutPassword
import kotlinx.android.synthetic.main.fragment_update_email.progressbar
import kotlinx.android.synthetic.main.fragment_update_password.*


class UpdatePasswordFragment : Fragment() {
    private val currentuser = FirebaseAuth.getInstance().currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPassword.visibility = View.VISIBLE
        layoutUpdatepassword.visibility = View.INVISIBLE

        button_authenticate.setOnClickListener {
            val password = edit_text_password.text.toString().trim()
            if (password.isEmpty()) {
                edit_text_password.error = "Password Required"
                edit_text_password.requestFocus()
                return@setOnClickListener

            }

            currentuser?.let { user ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                progressbar.visibility = View.VISIBLE

                user.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        progressbar.visibility = View.INVISIBLE
                        when {
                            task.isSuccessful -> {
                                layoutPassword.visibility = View.GONE
                                layoutUpdatepassword.visibility = View.VISIBLE
                            }
                            task.exception is FirebaseAuthInvalidCredentialsException -> {
                                edit_text_password.error = "Invalid Password"
                                edit_text_password.requestFocus()
                            }
                            else -> {
                                context?.toast(task.exception?.message!!)
                            }
                        }
                    }
            }
        }

        button_updatee.setOnClickListener {
            val password = edit_text_new_password.text.toString().trim()
            if(password.isEmpty() || password.length < 6){
                edit_text_new_password.error = "At least 6 character password required"
                edit_text_new_password.requestFocus()
                return@setOnClickListener

            }
            if(password != edit_text_new_Password_confirm.text.toString().trim()){
                edit_text_new_Password_confirm.error = "Password did not match "
                edit_text_new_Password_confirm.requestFocus()
                return@setOnClickListener
            }

            currentuser?.let { user ->
                progressbar.visibility = View.VISIBLE
                user.updatePassword(password)
                    .addOnCompleteListener {task ->
                        progressbar.visibility = View.INVISIBLE
                        if(task.isSuccessful){
                            val action = UpdatePasswordFragmentDirections.actionPasswordUpdated()
                            Navigation.findNavController(it).navigate(action)
                            context?.toast("Password Updated")
                        }else{
                            context?.toast(task?.exception?.message!!)
                        }

                    }
            }
        }
    }
}