package com.example.whatsappclone

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.whatsappclone.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var fAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var db: DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() = binding.run {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etSignUpEmail.text.toString()
            val password = binding.etSignUpPass.text.toString()
            val confirmPass = binding.etSignUpCfmPass.text.toString()
            if (TextUtils.isEmpty(email)) {
                etSignUpEmail.error = "Email is required to create an account"
            } else if (TextUtils.isEmpty(password)) {
                etSignUpPass.error = "Password is required to create an account"
            } else {
                if (password.length < 6) {
                    etSignUpPass.error = "Password must be greater than 6 characters"
                } else if (password != confirmPass) {
                    etSignUpCfmPass.error = "Both Password not matched"
                } else {
                    signUpProgressBar.visibility = View.VISIBLE
                    createAccount(email = email, pass = password)
                }
            }

        }
    }

    private fun createAccount(email: String, pass: String) = binding.apply {
        fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val userInfo = fAuth.currentUser?.uid.toString()
                db = fStore.collection("users").document(userInfo)
                val obj = mutableMapOf<String, String>()
                obj["userEmail"] = email
                obj["userPass"] = pass
                db.set(obj).addOnSuccessListener {
                    signUpProgressBar.visibility = View.GONE
                    Log.d(TAG, "User Account Created Successfully")
                }
            }
        }
    }

    companion object {
        val TAG = SignUp::class.java.simpleName
    }
}