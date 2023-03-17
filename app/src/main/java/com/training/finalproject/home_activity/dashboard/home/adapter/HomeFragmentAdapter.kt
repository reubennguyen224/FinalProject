package com.training.finalproject.home_activity.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.databinding.ItemChildRecyclerBinding
import com.training.finalproject.databinding.ItemNewProductBinding
import com.training.finalproject.databinding.ItemTitleLayoutBinding
import com.training.finalproject.model.BannerList
import com.training.finalproject.model.HomeRecyclerViewItem

class HomeFragmentAdapter : RecyclerView.Adapter<HomeRecyclerViewHolder>() {

    val viewPool = RecyclerView.RecycledViewPool()
    private val bannerAdapter = BannerAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return when (viewType) {
            TITLE -> HomeRecyclerViewHolder.TittleViewHolder(
                ItemTitleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onTitleClick
            )
            NEWPRODUCT -> HomeRecyclerViewHolder.ProductViewHolder(
                ItemNewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onProductClick
            )
            BANNER -> HomeRecyclerViewHolder.BannerViewHolder(
                ItemChildRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw java.lang.IllegalArgumentException("Ill Argument")
        }
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        when (holder) {
            is HomeRecyclerViewHolder.BannerViewHolder -> {
                val list = diff.currentList[position] as ArrayList<HomeRecyclerViewItem.Banner>
                bannerAdapter.items = list
                holder.bind(bannerAdapter)

            }
            is HomeRecyclerViewHolder.ProductViewHolder -> {
                val list = diff.currentList[position] as HomeRecyclerViewItem.Product
                holder.bind(list)
            }
            is HomeRecyclerViewHolder.TittleViewHolder -> {
                val list = diff.currentList[position] as HomeRecyclerViewItem.Title
                holder.bind(list)

            }
            is HomeRecyclerViewHolder.LoadMoreViewHolder -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (diff.currentList[position]) {
            is ArrayList<*> -> BANNER
            is HomeRecyclerViewItem.Product -> NEWPRODUCT
            is HomeRecyclerViewItem.Title -> TITLE
            else -> throw IllegalArgumentException("cant fetch data")
        }
    }

    override fun getItemCount(): Int = diff.currentList.size

    var onTitleClick: ((Int) -> Unit)? = null
    var onProductClick: ((HomeRecyclerViewItem.Product) -> Unit)? = null

    private val diffCall = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is BannerList && newItem is BannerList) true
            else if (oldItem is HomeRecyclerViewItem.Title && newItem is HomeRecyclerViewItem.Title) true
            else if (oldItem is HomeRecyclerViewItem.Product && newItem is HomeRecyclerViewItem.Product) {
                oldItem.id == newItem.id
            } else false
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is BannerList && newItem is BannerList) {
                oldItem.size == newItem.size && oldItem.containsAll(newItem) && newItem.containsAll(oldItem)
            } else if (oldItem is HomeRecyclerViewItem.Title && newItem is HomeRecyclerViewItem.Title) {
                oldItem.header == newItem.header
            } else if (oldItem is HomeRecyclerViewItem.Product && newItem is HomeRecyclerViewItem.Product) {
                oldItem == newItem
            } else false
        }

    }

    val diff = AsyncListDiffer(this, diffCall)
}

const val BANNER = 1
const val TITLE = 2
const val NEWPRODUCT = 3
const val LOADMORE = 4

