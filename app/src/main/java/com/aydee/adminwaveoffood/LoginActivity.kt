package com.aydee.adminwaveoffood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aydee.adminwaveoffood.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize Firebase auth
        auth = Firebase.auth
        // initialize Firebase database
        database = Firebase.database.reference

        // onClick Login button
        binding.btnLogin.setOnClickListener {
            // get text from edit text
            email = binding.etLoginEmail.text.toString().trim()
            password = binding.etLoginPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                showToast("Email or Password is blank")
            } else {
                // signing in
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("login is successful")
                        updateUi()
                    } else {
                        showToast("Login Failed")
                        Log.d("Account", "LoginActivity: Login Failed", task.exception)
                    }
                }
            }
        }

        // google sign in option
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("851004822751-214r8v5r44d0oilmrk88stco6mjuscu3.apps.googleusercontent.com")
            .requestEmail().build()
        // initialize google sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        // google sign in
        binding.btnGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        // onClick action if you don't have an account
        binding.txtDontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken.toString(), null)
                auth.signInWithCredential(credential).addOnCompleteListener {authTask ->
                    if(authTask.isSuccessful){
                        showToast("Google Sign In Successful")
                        updateUi()
                    }
                    else{
                        showToast("Google Sign In Failed")
                        Log.d("Account", "Google Login: Auth failed")
                    }
                }
            }
            else{
                showToast("Google Sign In Failed")
                Log.d("Account", "Google Login: task failed")
            }
        }
        else{
            showToast("Google Sign In Failed")
            Log.d("Account", "Google Login: result failed")
        }
    }

    // onStart Activity ->> To directly login if already signed in
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUi()
        }
    }

    // show Toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Intent to Main Activity
    private fun updateUi(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}