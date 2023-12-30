package com.aydee.adminwaveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aydee.adminwaveoffood.databinding.OrderDetailBinding
import com.bumptech.glide.Glide

class OrderDetailsAdapter(
    private val context: Context,
    private val foodNames: ArrayList<String>,
    private val foodImages: ArrayList<String>,
    private val foodPrices: ArrayList<String>,
    private val foodQuantity: ArrayList<Int>
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsAdapter.OrderDetailsViewHolder {
        val binding = OrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int = foodNames.size

    override fun onBindViewHolder(holder: OrderDetailsAdapter.OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class OrderDetailsViewHolder(private val binding: OrderDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                txtFoodName.text = foodNames[position]
                txtPrice.text = foodPrices[position]
                txtQuantity.text = foodQuantity[position].toString()
                val uri = Uri.parse(foodImages[position])
                Glide.with(context).load(uri).into(imgFoodImage)
            }
        }
    }
}