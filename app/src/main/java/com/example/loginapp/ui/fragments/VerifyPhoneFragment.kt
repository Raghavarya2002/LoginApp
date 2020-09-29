package com.example.loginapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.loginapp.R
import com.example.loginapp.utils.toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import java.util.concurrent.TimeUnit

class VerifyPhoneFragment : Fragment() {
    private var VerificationId :String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutPhone.visibility = View.VISIBLE
        layoutVerification.visibility = View.INVISIBLE

        button_send_verification.setOnClickListener {

            val phone = edit_text_phone.text.toString().trim()
            if(phone.isEmpty() || phone.length!= 10){
                edit_text_phone.error = "Enter a valid number"
                edit_text_phone.requestFocus()
                return@setOnClickListener
            }
            val phoneNumber = '+' + ccp.selectedCountryCode + phone
            PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(phoneNumber,
                60,
                    TimeUnit.SECONDS,
                    requireActivity(),
                    phoneAuthCallBacks

                )

            layoutPhone.visibility = View.INVISIBLE
            layoutVerification.visibility = View.VISIBLE
        }
        button_verify.setOnClickListener {
            val code = edit_text_code.text.toString().trim()
            if(code.isEmpty()){
                edit_text_code.error = "Code Required"
                edit_text_code.requestFocus()
                return@setOnClickListener

            }

            // if we have a code we need to generate phone auth credential object and firstly we need to check the verification id is not null , so we can check throgh "let"
            VerificationId?.let {
                val credential = PhoneAuthProvider.getCredential(it,code) // two parameters --> verification id and the code
                addPhoneNumber(credential)
            }
        }



    }


   private val phoneAuthCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential?) {
            phoneAuthCredential?.let {
                addPhoneNumber(phoneAuthCredential)
            }

        }

        override fun onVerificationFailed(exception: FirebaseException?) {
           context?.toast(exception?.message!!)
        }

       override fun onCodeSent(Verificationid: String?, token: PhoneAuthProvider.ForceResendingToken?) {
           super.onCodeSent(VerificationId, token)
           this@VerifyPhoneFragment .VerificationId = VerificationId
       }

    }

    private fun addPhoneNumber(phoneAuthCredential: PhoneAuthCredential) {
        val addOnCompleteListener = FirebaseAuth.getInstance()
            .currentUser?.updatePhoneNumber(phoneAuthCredential) // this function takes phoneauthcredential object and from this object it will add the phone number that was verified to this user
            //add complete listener to get the callback when the task is completed
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    context?.toast("Phone Added")
                    val action = VerifyPhoneFragmentDirections.actionPhoneVerified()
                    Navigation.findNavController(button_verify).navigate(action)
                } else {
                    context?.toast(task.exception?.message!!)
                }

            }
    }


}