//package com.training.finalproject.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import com.training.finalproject.databinding.ItemLoadMoreBinding
//import com.training.finalproject.databinding.ItemNewProductBinding
//import com.training.finalproject.model.HomeRecyclerViewItem
//
//class ProductListAdapter : RecyclerView.Adapter<ViewHolder>() {
//
//    var onClick: ((HomeRecyclerViewItem.Product) -> Unit)? = null
//
//    inner class MyViewHolder(binding: ItemNewProductBinding) :
//        ViewHolder(binding.root) {
//        val img = binding.imgNewProduct
//        val nameProduct = binding.txtNewProductName
//        val nameStore = binding.txtManufactoryName
//        val price = binding.txtPrice
//        val originalPrice = binding.txtOriginalPrice
//        val rate = binding.txtProductRate
//
//        init {
//            binding.root.setOnClickListener {
//                if (differ.currentList[adapterPosition] is HomeRecyclerViewItem.Product)
//                    onClick?.invoke(differ.currentList[adapterPosition] as HomeRecyclerViewItem.Product)
//            }
//        }
//    }
//
//    class LoadMoreHolder(binding: ItemLoadMoreBinding) : ViewHolder(binding.root) {
//        val progressBar = binding.progressBar
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        if (viewType == VIEW_TYPE_ITEM)
//            return MyViewHolder(
//                ItemNewProductBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        else
//            return LoadMoreHolder(
//                ItemLoadMoreBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//    }
//
//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }
//
//    private fun bindingItem(holder: MyViewHolder, position: Int) {
//        val product = differ.currentList[position]
//        holder.img.setImageResource(product.img)
//        holder.nameProduct.text = product.nameProduct
//        holder.nameStore.text = product.nameManufactory
//        holder.originalPrice.text = buildString {
//            append("$")
//            append(product.orginalPrice)
//        }
//        holder.price.text = buildString {
//            append("$")
//            append(product.currentPrice.toString())
//        }
//        holder.rate.text = product.rateTotal.toString()
//    }
//
//    private fun bindingLoadMore(holder: LoadMoreHolder, position: Int) {
//        holder.progressBar.progress = 10
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (holder is MyViewHolder) {
//            bindingItem(holder, position)
//        } else if (holder is LoadMoreHolder)
//            bindingLoadMore(holder, position)
//
//    }
//
//    private val diffCall = object : DiffUtil.ItemCallback<HomeRecyclerViewItem.Product>() {
//        override fun areItemsTheSame(
//            oldItem: HomeRecyclerViewItem.Product,
//            newItem: HomeRecyclerViewItem.Product
//        ): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(
//            oldItem: HomeRecyclerViewItem.Product,
//            newItem: HomeRecyclerViewItem.Product
//        ): Boolean {
//            return oldItem.nameProduct == newItem.nameProduct && oldItem.img == newItem.img && oldItem.nameManufactory == newItem.nameManufactory && oldItem.currentPrice == newItem.currentPrice && oldItem.orginalPrice == newItem.orginalPrice && oldItem.rateTotal == newItem.rateTotal
//        }
//
//    }
//    val differ = AsyncListDiffer(this, diffCall)
//    override fun getItemViewType(position: Int): Int {
//        return if (differ.currentList[position] == null) {
//            VIEW_TYPE_LOADING
//        } else VIEW_TYPE_ITEM
//    }
//}
//
//const val VIEW_TYPE_LOADING = 1
//const val VIEW_TYPE_ITEM = 0