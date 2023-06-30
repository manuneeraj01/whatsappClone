package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.whatsappclone.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInOption : GoogleSignInOptions
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var resultLauncher:ActivityResultLauncher<Intent>
    private val RC_SIGN_IN = 1011

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    private fun clickListeners() = binding.run {
        btnLogin.setOnClickListener {
            val email = etEmailLogin.text.toString()
            val pass = etPasswordLogin.text.toString()
            if (TextUtils.isEmpty(email)) {
                etEmailLogin.error = "Email is required to Login"
            }
            if (TextUtils.isEmpty(pass)) {
                etPasswordLogin.error = "Password is required to Login"
            } else {
                if (pass.length < 6) {
                    etPasswordLogin.error = "Password must be greater than 6 character"
                } else {
                    login(email = email, pass = pass)
                }
            }
        }

        btnLoginWithGoogle.setOnClickListener {

        }

    }

    private fun login(email: String, pass: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                if (isAdded && context != null) {
                    Toast.makeText(requireContext(), "Login Successfully", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

}