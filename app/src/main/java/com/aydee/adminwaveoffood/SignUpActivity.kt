package com.aydee.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aydee.adminwaveoffood.databinding.ActivitySignUpBinding
import com.aydee.adminwaveoffood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize firebase auth
        auth = Firebase.auth
        // initialize firebase database
        database = Firebase.database.reference

        // binding list to TextInputLayout view
        val locationList = listOf("Patna", "Jaipur", "Kolkata", "Delhi", "Bangalore")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        binding.listOfLocation.setAdapter(adapter)

        binding.btnCreateAccount.setOnClickListener {
            // get text from edit text
            userName = binding.etUserName.text.toString().trim()
            nameOfRestaurant = binding.etNameOfRestaurant.text.toString().trim()
            email = binding.etEmail.text.toString().trim()
            password = binding.etPassword.text.toString().trim()

            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                showToast("Fill all the details properly")
            }
            else{
                createAccount()
            }
        }

        // onClick action if  already have an account
        binding.txtAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount() {
        // Create account through firebase authentication
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                showToast("Sign Up is Successful !!!")
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                showToast("Sign Up Failure !!!")
                Log.d("Account", "SignUpActivity: sign up failed", task.exception)
            }
        }
    }

    // save data in database
    private fun saveUserData() {
        val user = UserModel(name = userName, restaurantName =  nameOfRestaurant, email = email, password = password)
        val userId = auth.currentUser?.uid
        database.child("admin").child(userId!!).setValue(user)
    }

    // show Toast message
    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}