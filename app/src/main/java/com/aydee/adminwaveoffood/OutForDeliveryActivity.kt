package com.aydee.adminwaveoffood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aydee.adminwaveoffood.adapter.OutForDeliveryAdapter
import com.aydee.adminwaveoffood.databinding.ActivityOutForDeliveryBinding
import com.aydee.adminwaveoffood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {

    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompletedOrder: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // retrieve and display completed orders
        retrieveCompletedOrder()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun retrieveCompletedOrder() {
        // initialize firebase
        database = FirebaseDatabase.getInstance()
        val completedOrderReference = database.reference.child("CompletedOrders").orderByChild("currentTime")
        completedOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list before populating
                listOfCompletedOrder.clear()
                for(orderSnapshot in snapshot.children){
                    val completedOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completedOrder?.let {
                        listOfCompletedOrder.add(it)
                    }
                }
                // reverse the list to display latest order first
                listOfCompletedOrder.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun setDataIntoRecyclerView() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for(order in listOfCompletedOrder){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }

        val adapter = OutForDeliveryAdapter(customerName, moneyStatus)
        binding.outForDeliveryRV.layoutManager = LinearLayoutManager(this)
        binding.outForDeliveryRV.adapter = adapter
    }
}