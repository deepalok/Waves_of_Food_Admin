package com.aydee.adminwaveoffood.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aydee.adminwaveoffood.databinding.OutForDeliveryItemBinding

class OutForDeliveryAdapter(
    private val customerName: MutableList<String>,
    private val paymentStatus: MutableList<Boolean>
) : RecyclerView.Adapter<OutForDeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OutForDeliveryAdapter.DeliveryViewHolder {
        val binding =
            OutForDeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int = customerName.size

    override fun onBindViewHolder(holder: OutForDeliveryAdapter.DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DeliveryViewHolder(private val binding: OutForDeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            binding.apply {
                txtCustomerName.text = customerName[position]
                if (paymentStatus[position]) {
                    txtPaymentStatus.text = "Received"
                } else {
                    txtPaymentStatus.text = "Not Received"
                }
                //cvDeliveryStatus.setCardBackgroundColor(deliveryStatus[position])
                val colorMap = mapOf(
                    true to Color.GREEN, false to Color.RED
                )
                txtPaymentStatus.setTextColor(colorMap[paymentStatus[position]] ?: Color.GRAY)
                cvDeliveryStatus.backgroundTintList =
                    ColorStateList.valueOf(colorMap[paymentStatus[position]] ?: Color.GRAY)
            }
        }
    }


}