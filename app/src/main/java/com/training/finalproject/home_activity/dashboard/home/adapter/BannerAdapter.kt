package com.training.finalproject.home_activity.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.training.finalproject.databinding.ItemBannerLayoutBinding
import com.training.finalproject.model.HomeRecyclerViewItem

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    var items = ArrayList<HomeRecyclerViewItem.Banner>()

    inner class BannerViewHolder(val binding: ItemBannerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: HomeRecyclerViewItem.Banner) {
            Glide.with(binding.root.context).load(banner.image).into(binding.imgBanner)
            binding.root.setOnClickListener {
                onClick?.invoke(banner)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ItemBannerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    var onClick: ((HomeRecyclerViewItem.Banner) -> Unit)? = null
}