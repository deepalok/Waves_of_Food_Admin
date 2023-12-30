package com.aydee.adminwaveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aydee.adminwaveoffood.databinding.PendingOrderItemBinding
import com.bumptech.glide.Glide

class PendingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val pendingQuantity: MutableList<String>,
    private val pendingImage: MutableList<String>,
    private val itemClicked: onItemClicked
) :
    RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface onItemClicked {
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingOrderAdapter.PendingOrderViewHolder {
        val binding =
            PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int = customerName.size

    override fun onBindViewHolder(
        holder: PendingOrderAdapter.PendingOrderViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            var isAccepted = false

            binding.apply {
                txtPendingOrderCustomerName.text = customerName[position]
                txtPendingOrderQuantity.text = pendingQuantity[position]
                val uri = Uri.parse(pendingImage[position])
                Glide.with(context).load(uri).into(imgPendingOrderImage)

                btnAccept.apply {
                    text = if (isAccepted) {
                        "Dispatch"
                    } else {
                        "Accept"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }

            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

}