package com.training.finalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.databinding.ItemColorListBinding

class ColorAdapter: RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    class ColorViewHolder(binding: ItemColorListBinding): RecyclerView.ViewHolder(binding.root) {
        val color = binding.nameColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
       return ColorViewHolder(ItemColorListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int  = diff.currentList.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.color.text = diff.currentList[position]
    }

    private val diffCall =object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
           return oldItem == newItem
        }
    }

    val diff = AsyncListDiffer(this, diffCall)
}