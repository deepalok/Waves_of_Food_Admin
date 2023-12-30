package com.aydee.adminwaveoffood

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aydee.adminwaveoffood.adapter.AllItemAdapter
import com.aydee.adminwaveoffood.databinding.ActivityAllItemBinding
import com.aydee.adminwaveoffood.model.ItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    private var menuItems: ArrayList<ItemModel> = arrayListOf()
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // retrieve the menu items to show
        retrieveMenuItems()

        // back button to finish the current activity
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.getReference("menu")
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear all items before populating
                menuItems.clear()
                // get all the menu items from the snapshot of "menu" node in database
                // loop for through each food item
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(ItemModel::class.java)
                    // add the item in menu list
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                // call setAdapter function
                setAdapter()
            }

            // if any error occurs then this function will be called
            override fun onCancelled(error: DatabaseError) {
                Log.d("All Item", "AllItemActivity: ${error.toException()}")
            }
        })
    }

    // set adapter with AllItemAdapter
    private fun setAdapter() {
        val adapter = AllItemAdapter(this, menuItems) { position ->
            deleteMenuItems(position)
        }
        binding.allItemRV.layoutManager = LinearLayoutManager(this)
        binding.allItemRV.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                menuItems.removeAt(position)
                binding.allItemRV.adapter?.notifyItemRemoved(position)
            } else {
                Toast.makeText(this, "Item not deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}