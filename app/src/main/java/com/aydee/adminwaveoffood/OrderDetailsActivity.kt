package com.aydee.adminwaveoffood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aydee.adminwaveoffood.adapter.OrderDetailsAdapter
import com.aydee.adminwaveoffood.databinding.ActivityOrderDetailsBinding
import com.aydee.adminwaveoffood.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {

    val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getDataFromIntent()

        binding.btnBackView.setOnClickListener {
            finish()
        }
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails.let {
            userName = receivedOrderDetails.userName
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            totalPrice = receivedOrderDetails.totalPrice
            foodNames = receivedOrderDetails.foodNames as ArrayList<String>
            foodImages = receivedOrderDetails.foodImages as ArrayList<String>
            foodPrices = receivedOrderDetails.foodPrices as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>

            setUserDetails()
            setAdapter()
        }
    }

    private fun setAdapter() {
        val adapter = OrderDetailsAdapter(this, foodNames, foodImages, foodPrices, foodQuantity)
        binding.orderDetailsRV.layoutManager = LinearLayoutManager(this)
        binding.orderDetailsRV.adapter = adapter
    }

    private fun setUserDetails() {
        binding.apply {
            txtName.setText(userName)
            txtAddress.setText(address)
            txtPhone.setText(phoneNumber)
            txtTotalPrice.setText(totalPrice)
        }
    }
}