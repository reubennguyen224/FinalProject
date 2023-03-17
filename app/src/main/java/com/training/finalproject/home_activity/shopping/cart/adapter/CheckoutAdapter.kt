package com.training.finalproject.home_activity.shopping.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.training.finalproject.databinding.ItemProductCheckoutBinding
import com.training.finalproject.home_activity.shopping.cart.model.CartItem

class CheckoutAdapter : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    class CheckoutViewHolder(val binding: ItemProductCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            Glide.with(binding.root.context).load(cartItem.product?.image).into(binding.imgProduct)
            binding.txtProductName.text = cartItem.product?.name
            binding.txtPriceProduct.text = "$" + cartItem.product?.sale_price
            binding.txtQuantityProduct.text = "Quantity ${cartItem.number}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        return CheckoutViewHolder(
            ItemProductCheckoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        holder.bind(diff.currentList[position])
    }

    val diffCall = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product?.id == newItem.product?.id && oldItem.number == newItem.number && oldItem.product?.sale_price == newItem.product?.sale_price
        }
    }

    val diff = AsyncListDiffer(this, diffCall)
}