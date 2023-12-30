package com.aydee.adminwaveoffood

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aydee.adminwaveoffood.databinding.ActivityProfileBinding
import com.aydee.adminwaveoffood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("admin")

        retrieveAdminData()

        binding.txtEdit.setOnClickListener {
            binding.apply {
                etName.isEnabled = true
                etAddress.isEnabled = true
                etPhone.isEnabled = true
                etEmail.isEnabled = true
                etPassword.isEnabled = true
                etName.requestFocus()

                btnSaveInformation.visibility = View.VISIBLE
            }
        }

        binding.btnSaveInformation.setOnClickListener {
            updateAdminData()
            binding.apply {
                etName.isEnabled = false
                etAddress.isEnabled = false
                etPhone.isEnabled = false
                etEmail.isEnabled = false
                etPassword.isEnabled = false
                btnSaveInformation.visibility = View.GONE
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun updateAdminData() {
        val updateName = binding.etName.text.toString()
        val updateRestaurant = binding.etRestaurantName.text.toString()
        val updateAddress = binding.etAddress.text.toString()
        val updatePhone = binding.etPhone.text.toString()
        val updateEmail = binding.etEmail.text.toString()
        val updatePassword = binding.etPassword.text.toString()

        val userData = UserModel(updateName, updateRestaurant, updateAddress, updatePhone, updateEmail, updatePassword)
        val adminUid = auth.currentUser?.uid
        val userReference = adminReference.child(adminUid!!)
        userReference.setValue(userData).addOnSuccessListener {
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            // update email and password for firebase authentication
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }
        .addOnFailureListener {
            Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
        }
//        if(adminUid != null) {
//            val userReference = adminReference.child(adminUid)
//            userReference.child("name").setValue(updateName)
//            userReference.child("address").setValue(updateAddress)
//            userReference.child("phone").setValue(updatePhone)
//            userReference.child("email").setValue(updateEmail)
//            userReference.child("password").setValue(updatePassword)
//
//            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
//            // update email and password for firebase authentication
//            auth.currentUser?.updateEmail(updateEmail)
//            auth.currentUser?.updatePassword(updatePassword)
//        }
//        else {
//            Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun retrieveAdminData() {
        val userUid = auth.currentUser?.uid
        if (userUid != null) {
            val userReference = adminReference.child(userUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val adminName = snapshot.child("name").getValue()
                        val restaurantName = snapshot.child("restaurantName").value
                        val address = snapshot.child("address").getValue()
                        val phone = snapshot.child("phone").getValue()
                        val email = snapshot.child("email").getValue()
                        val password = snapshot.child("password").getValue()

                        setDataToView(adminName, restaurantName, address, phone, email, password)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun setDataToView(
        adminName: Any?,
        restaurantName: Any?,
        address: Any?,
        phone: Any?,
        email: Any?,
        password: Any?
    ) {
        binding.apply {
            etName.setText(adminName.toString())
            etRestaurantName.setText(restaurantName.toString())
            etAddress.setText(address.toString())
            etPhone.setText(phone.toString())
            etEmail.setText(email.toString())
            etPassword.setText(password.toString())
        }
    }
}