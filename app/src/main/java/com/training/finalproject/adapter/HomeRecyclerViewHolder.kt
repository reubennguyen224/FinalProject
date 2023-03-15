package com.training.finalproject.adapter

import android.util.Size
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.training.finalproject.databinding.ItemBannerLayoutBinding
import com.training.finalproject.databinding.ItemChildRecyclerBinding
import com.training.finalproject.databinding.ItemHomeFragmentLayoutBinding
import com.training.finalproject.databinding.ItemLoadMoreBinding
import com.training.finalproject.databinding.ItemNewProductBinding
import com.training.finalproject.databinding.ItemTitleLayoutBinding
import com.training.finalproject.model.HomeRecyclerViewItem
import com.training.finalproject.utils.ProductDecoration

sealed class HomeRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root){
    class TittleViewHolder(private val binding: ItemTitleLayoutBinding, onClick: ((Int) -> Unit)?): HomeRecyclerViewHolder(binding){
        fun bind(title: HomeRecyclerViewItem.Title){
            binding.txtNameOfList.text = title.header
        }

        init {
            binding.txtSeeMore.setOnClickListener {
                onClick?.invoke(adapterPosition)
            }
        }
    }
    class BannerViewHolder(private val binding: ItemChildRecyclerBinding): HomeRecyclerViewHolder(binding){
        fun bind(bannerAdapter: BannerAdapter){
            binding.listItem.apply {
                adapter = bannerAdapter
                layoutManager = LinearLayoutManager(
                    binding.listItem.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val productDecoration = ProductDecoration(20)
                addItemDecoration(productDecoration)
                setHasFixedSize(true)
                setItemViewCacheSize(15)
            }
        }
    }
    class ProductViewHolder(private val binding: ItemNewProductBinding, onClick: ((Int) -> Unit)?): HomeRecyclerViewHolder(binding){
        fun bind(product: HomeRecyclerViewItem.Product){
            Glide.with(binding.root.context).load(product.image).fitCenter().into(binding.imgNewProduct)
            binding.txtNewProductName.text = product.name
            binding.txtManufactoryName.text = product.seller
            binding.txtPrice.text = "$"+product.sale_price
            binding.txtOriginalPrice.text = "$"+product.price
            binding.txtProductRate.text = product.star.toString()
        }

        init {
            binding.root.setOnClickListener {
                onClick?.invoke(adapterPosition)
            }
        }
    }
    class LoadMoreViewHolder(private val binding: ItemLoadMoreBinding): HomeRecyclerViewHolder(binding){

    }
}