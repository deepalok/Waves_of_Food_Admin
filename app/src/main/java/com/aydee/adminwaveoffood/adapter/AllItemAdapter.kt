package com.aydee.adminwaveoffood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aydee.adminwaveoffood.databinding.AllItemBinding
import com.aydee.adminwaveoffood.model.ItemModel
import com.bumptech.glide.Glide

class AllItemAdapter(
    private val context : Context,
    private val itemDetails: ArrayList<ItemModel>,
    private val onDeleteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>() {

    private val itemQuantity = IntArray(itemDetails.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val binding = AllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemViewHolder(binding)
    }

    override fun getItemCount(): Int = itemDetails.size

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AllItemViewHolder(private val binding: AllItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                txtItemQuantity.text = quantity.toString()

                txtAllItemFoodName.text = itemDetails[position].foodName
                txtAllItemFoodPrice.text = itemDetails[position].foodPrice
                Glide.with(context).load(itemDetails[position].foodImage).into(imgAllItemImage)

                btnPlus.setOnClickListener {
                    itemIncreament(position, binding)
                }
                btnMinus.setOnClickListener {
                    itemDecreament(position, binding)
                }
                btnDelete.setOnClickListener {
//                    itemDelete(position)
                    onDeleteClickListener(position)
                }
            }
        }
    }

    private fun itemIncreament(position: Int, binding: AllItemBinding) {
        if (itemQuantity[position] < 10) {
            itemQuantity[position]++
            binding.txtItemQuantity.text = itemQuantity[position].toString()
        }
    }

    private fun itemDecreament(position: Int, binding: AllItemBinding) {
        if (itemQuantity[position] > 1) {
            itemQuantity[position]--
            binding.txtItemQuantity.text = itemQuantity[position].toString()
        }
    }

    private fun itemDelete(position: Int) {
        itemDetails.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemDetails.size)
    }
}