package com.training.finalproject.home_activity.dashboard.home.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.training.finalproject.databinding.ItemChildRecyclerBinding
import com.training.finalproject.databinding.ItemLoadMoreBinding
import com.training.finalproject.databinding.ItemNewProductBinding
import com.training.finalproject.databinding.ItemTitleLayoutBinding
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.utils.ProductDecoration

sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class TittleViewHolder(private val binding: ItemTitleLayoutBinding, onClick: ((Int) -> Unit)?) :
        HomeRecyclerViewHolder(binding) {
        fun bind(title: HomeRecyclerViewItem.Title) {
            binding.txtNameOfList.text = title.header
        }

        init {
            binding.txtSeeMore.setOnClickListener {
                onClick?.invoke(adapterPosition)
            }
        }
    }

    class BannerViewHolder(private val binding: ItemChildRecyclerBinding) :
        HomeRecyclerViewHolder(binding) {
        fun bind(bannerAdapter: BannerAdapter) {
            binding.listItem.apply {
                adapter = bannerAdapter
                layoutManager = LinearLayoutManager(
                    binding.listItem.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val productDecoration = ProductDecoration(25)
                addItemDecoration(productDecoration)
                while (itemDecorationCount > 1) removeItemDecorationAt(1)
                setHasFixedSize(true)
                setItemViewCacheSize(15)
            }
        }
    }

    class ProductViewHolder(
        private val binding: ItemNewProductBinding,
        val onClick: ((HomeRecyclerViewItem.Product) -> Unit)?
    ) :
        HomeRecyclerViewHolder(binding) {
        fun bind(product: HomeRecyclerViewItem.Product) {
            Glide.with(binding.root.context).load(product.image).fitCenter()
                .into(binding.imgNewProduct)
            binding.txtNewProductName.text = product.name
            binding.txtManufactoryName.text = product.seller
            binding.txtPrice.text = "$" + product.sale_price
            binding.txtOriginalPrice.text = "$" + product.price
            binding.root.setOnClickListener {
                onClick?.invoke(product)
            }
            binding.txtProductRate.text = product.star.toString()
        }

    }

    class LoadMoreViewHolder(private val binding: ItemLoadMoreBinding) :
        HomeRecyclerViewHolder(binding) {

    }
}