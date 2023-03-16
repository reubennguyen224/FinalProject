package com.training.finalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.training.finalproject.R
import com.training.finalproject.databinding.ItemCartLayoutBinding
import com.training.finalproject.model.CartItem

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var onCheckClick: ((Int) -> Unit)? = null
    var onDeleteClick: ((Int) -> Unit)? = null
    var onUpdateNumberClick: ((Boolean, Int) -> Unit)? = null

    inner class CartViewHolder(val binding: ItemCartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(cartItem: CartItem) {
            binding.txtNameProduct.text = cartItem.product?.name
            Glide.with(binding.root.context).load(cartItem.product?.image).into(binding.imgProduct)
            binding.txtNumberProduct.text = cartItem.number.toString()
            if (cartItem.checked)
                binding.chkItem.setImageResource(R.drawable.ic_checked_box)
            else binding.chkItem.setImageResource(R.drawable.ic_check_box)
            binding.txtPrice.text = "$" + cartItem.product?.sale_price.toString()
        }

        init {
            binding.chkItem.setOnClickListener {
                onCheckClick?.invoke(adapterPosition)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteClick?.invoke(adapterPosition)
            }
            binding.btnAddProduct.setOnClickListener {
                onUpdateNumberClick?.invoke(true, adapterPosition)
            }

            binding.btnSubtractProduct.setOnClickListener {
                onUpdateNumberClick?.invoke(false, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.onBind(diff.currentList[position])
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            if (payloads[0] == true) {
                if (diff.currentList[position].checked)
                    holder.binding.chkItem.setImageResource(R.drawable.ic_checked_box)
                else holder.binding.chkItem.setImageResource(R.drawable.ic_check_box)
            }
        } else
            super.onBindViewHolder(holder, position, payloads)
    }

    private val diffCall = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.checked == newItem.checked && oldItem.number == newItem.number
        }

        override fun getChangePayload(oldItem: CartItem, newItem: CartItem): Any? {
//            return super.getChangePayload(oldItem, newItem)
            return if (oldItem.checked != newItem.checked) true else null
        }
    }
    val diff = AsyncListDiffer(this, diffCall)
}