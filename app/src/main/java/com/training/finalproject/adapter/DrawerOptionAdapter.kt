package com.training.finalproject.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.training.finalproject.databinding.ItemOptionsLayoutBinding
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.DiffUtilCallbackOption
import com.training.finalproject.R
import com.training.finalproject.model.Option
import com.training.finalproject.model.OptionDrawer

class DrawerOptionAdapter : RecyclerView.Adapter<DrawerOptionAdapter.DrawerHolder>() {
    lateinit var mListener: OnClickListener

    interface OnClickListener {
        fun onClick(position: Int)
    }

    class DrawerHolder(binding: ItemOptionsLayoutBinding, listener: OnClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        val icon = binding.imgIconOption
        val viewChosen = binding.imgChosenOption
        val nameOption = binding.txtNameOption
        init {
            binding.root.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }

    fun setOnClickListener(listener: OnClickListener){
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerHolder {
        return DrawerHolder(
            ItemOptionsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: DrawerHolder, position: Int) {
        holder.icon.setBackgroundResource(differ.currentList[position].icon)
        holder.nameOption.text = differ.currentList[position].nameOption
        if (differ.currentList[position].status){
            holder.viewChosen.setBackgroundResource(R.color.price)
            holder.nameOption.setTextColor(Color.parseColor("#06AB8D"))
            holder.icon.setColorFilter(Color.parseColor("#06AB8D"))
        } else {
            holder.viewChosen.setBackgroundResource(R.color.transfer)
            holder.nameOption.setTextColor(Color.parseColor("#8B9E9E"))
            holder.icon.setColorFilter(Color.parseColor("#8B9E9E"))
        }
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DrawerHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        } else{
            val option = payloads[0] as Bundle
            for (key in option.keySet()){
                if (key ==  "statusLogin"){
                    holder.icon.setBackgroundResource(differ.currentList[position].icon)
                    holder.nameOption.text = differ.currentList[position].nameOption
                    if (differ.currentList[position].status){
                        holder.viewChosen.setBackgroundResource(R.color.price)
                    } else {
                        holder.viewChosen.setBackgroundResource(R.color.transfer)
                    }
                }
            }
        }

    }

    private val diffCall = object : DiffUtil.ItemCallback<OptionDrawer>(){
        override fun areItemsTheSame(oldItem: OptionDrawer, newItem: OptionDrawer): Boolean { //add and remove
            return oldItem.nameOption == newItem.nameOption
        }

        override fun areContentsTheSame(oldItem: OptionDrawer, newItem: OptionDrawer): Boolean { // update
            return oldItem.status == newItem.status
        }

        override fun getChangePayload(oldItem: OptionDrawer, newItem: OptionDrawer): Any? { //refresh 1 component in total
//            return super.getChangePayload(oldItem, newItem)
            val diffBundle = Bundle()
            if (oldItem.status != newItem.status) {
                diffBundle.putBoolean("statusLogin", newItem.status)
            }
            if (diffBundle.size() == 0) return null
            return diffBundle
        }
    }
    val differ = AsyncListDiffer(this, diffCall)
}