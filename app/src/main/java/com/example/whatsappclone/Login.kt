package com.example.whatsappclone

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.whatsappclone.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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
        googleSignIn()
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
//            createRequest()
        }

        /*resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                var launchData = it.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(launchData)
                try{
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "Firebase Auth With Google : $account")
                    firebaseAuthWithGoogle(account?.idToken)
                }
                catch (e:Exception){
                    Log.d(TAG, "Google Sign IN Failed : $e")
                }
            }

        }*/

    }
    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        binding.btnLoginWithGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1)
        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    /*private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Toast.makeText(requireContext(),"signedIn",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginUserFragment_to_signUpUserFragment)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("TAG", "signInResult:failed code=${e.statusCode}")
        }
    }*/
    @RequiresApi(Build.VERSION_CODES.S)
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI
            Toast.makeText(
                requireContext(),
                "Signed in with Google: ${account?.email}",
                Toast.LENGTH_SHORT
            ).show()
//                findNavController().navigate(R.id.action_loginUserFragment_to_signUpUserFragment)

            // Perform any additional actions or navigation

        } catch (e: ApiException) {
            Toast.makeText(
                requireContext(),
                "failed in with Google :$e",
                Toast.LENGTH_SHORT
            ).show()
            Log.e("LoginFragment", "Google sign-in failed: ${e.statusCode}")
            // Handle sign-in failure
        }
    }

    /*private fun createRequest() {
        googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken("getString(R.string.default_web_client_id)")
            .requestEmail()
            .build()
    }*/

   /* private fun firebaseAuthWithGoogle(idToken: String?) {

    }*/

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

    companion object{
        private val TAG = Login::class.java.simpleName
    }
}