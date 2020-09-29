package com.example.loginapp.ui.fragments

import android.os.Bundle
import android.util.Patterns
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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_update_email.*
import kotlinx.android.synthetic.main.fragment_update_email.edit_text_email


class UpdateEmailFragment : Fragment() {
   private val currentuser = FirebaseAuth.getInstance().currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutPassword.visibility =  View.VISIBLE
        layoutUpdateEmail.visibility = View.INVISIBLE

        button_authenticate.setOnClickListener {
            val password = edit_text_password.text.toString().trim()
            if(password.isEmpty()){
                edit_text_password.error = "Password Required"
                edit_text_password.requestFocus()
                return@setOnClickListener

            }

            currentuser?.let {user->
                val credential = EmailAuthProvider.getCredential(user.email!! , password)
                progressbar.visibility = View.VISIBLE

                user.reauthenticate(credential)
                    .addOnCompleteListener {task->
                        progressbar.visibility = View.INVISIBLE
                        when {
                            task.isSuccessful -> {
                                layoutPassword.visibility =  View.INVISIBLE
                                layoutUpdateEmail.visibility = View.VISIBLE
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
// when the user entered correct password
        button_update.setOnClickListener {view->
            val email = edit_text_email.text.toString().trim()
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
            progressbar.visibility = View.VISIBLE
            currentuser?.let {user->
                user.updateEmail(email)
                    .addOnCompleteListener {task->
                        progressbar.visibility = View.INVISIBLE
                        if(task.isSuccessful){
                            val action = UpdateEmailFragmentDirections.actionEmailUpdated()
                            Navigation.findNavController(view).navigate(action)
                        }else{
                            context?.toast(task.exception?.message!!)
                        }
                        
                    }

            }


        }
    }



}