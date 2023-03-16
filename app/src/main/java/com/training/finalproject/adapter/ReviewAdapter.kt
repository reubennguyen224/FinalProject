package com.training.finalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.R
import com.training.finalproject.databinding.ItemReviewLayoutBinding
import com.training.finalproject.model.ReviewItem
import com.training.finalproject.utils.convertTimestamp

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    class ReviewViewHolder(binding: ItemReviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imgAvatar = binding.imgAvatar
        val txtName = binding.txtUserName
        val rate = binding.rateBarUser
        val date = binding.txtDayFeedBack
        val feedback = binding.txtFeedback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemReviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = diff.currentList[position]
        holder.imgAvatar.setImageResource(R.drawable.ic_myprofile)
        holder.txtName.text = item.user_name
        holder.rate.rating = item.rate.toFloat()
        holder.feedback.text = item.content
        holder.date.text = convertTimestamp(item.time)
    }

    private val diffCall = object : DiffUtil.ItemCallback<ReviewItem>() {
        override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
            return oldItem.content == newItem.content && oldItem.time == newItem.time && oldItem.rate == newItem.rate && oldItem.user_name == newItem.user_name
        }

    }

    val diff = AsyncListDiffer(this, diffCall)
}