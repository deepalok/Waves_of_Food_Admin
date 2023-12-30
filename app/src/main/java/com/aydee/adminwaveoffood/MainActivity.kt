package com.aydee.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aydee.adminwaveoffood.databinding.ActivityMainBinding
import com.aydee.adminwaveoffood.model.OrderDetails
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var completedOrdersReference: DatabaseReference
    private lateinit var orderDetailsReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        completedOrdersReference = database.reference.child("CompletedOrders")
        orderDetailsReference = database.reference.child("OrderDetails")

        binding.btnAddMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.btnAllItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnCreateNewUser.setOnClickListener {
            val intent = Intent(this, CreateNewUserActivity::class.java)
            startActivity(intent)
        }

        binding.btnOrderDispatch.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogOut.setOnClickListener {
            // google sign in option
            val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("851004822751-214r8v5r44d0oilmrk88stco6mjuscu3.apps.googleusercontent.com")
                .requestEmail().build()
            // google options sign out
            GoogleSignIn.getClient(this, googleSignInOption).signOut()
            // sign out from google
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.txtPendingOrder.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        showPendingOrders()
        showCompletedOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        // add totalPrice of all CompletedOrders
        val listOfTotalEarning = mutableListOf<Int>()
        completedOrdersReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ordersSnapshot in snapshot.children){
                    val completedOrderDetails = ordersSnapshot.getValue(OrderDetails::class.java)
                    completedOrderDetails?.totalPrice?.replace("$ ", "")?.toIntOrNull()
                        ?.let {
                            listOfTotalEarning.add(it)
                        }
                }
                binding.txtWholeTimeEarning.text = "$" + listOfTotalEarning.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun showCompletedOrders() {
        // display number of child nodes in CompletedOrders
        completedOrdersReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtNumOfCompletedOrders.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun showPendingOrders() {
        // display number of child nodes in OrderDetails
        orderDetailsReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtNumOfPendingOrders.text = snapshot.childrenCount.toString()
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }
}