package com.training.finalproject.home_activity.drawer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.finalproject.R
import com.training.finalproject.databinding.ItemOptionsLayoutBinding

class DrawerOptionAdapter : RecyclerView.Adapter<DrawerOptionAdapter.DrawerHolder>() {

    var onClick: ((OptionDrawer) -> Unit)? = null

    inner class DrawerHolder(val binding: ItemOptionsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: OptionDrawer) {
            binding.imgIconOption.setImageResource(option.icon)
            binding.txtNameOption.text = option.nameOption
            if (option.status) {
                binding.imgChosenOption.setBackgroundResource(R.color.price)
                binding.txtNameOption.setTextColor(Color.parseColor("#06AB8D"))
                binding.imgIconOption.setColorFilter(Color.parseColor("#06AB8D"))
            } else {
                binding.imgChosenOption.setBackgroundResource(R.color.transfer)
                binding.txtNameOption.setTextColor(Color.parseColor("#8B9E9E"))
                binding.imgIconOption.setColorFilter(Color.parseColor("#8B9E9E"))
            }
            setIsRecyclable(false)
            binding.root.setOnClickListener {
                onClick?.invoke(option)
            }
        }

        val icon = binding.imgIconOption
        val viewChosen = binding.imgChosenOption
        val nameOption = binding.txtNameOption

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerHolder {
        return DrawerHolder(
            ItemOptionsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DrawerHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DrawerHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val option = payloads[0] as Bundle
            for (key in option.keySet()) {
                if (key == "statusLogin") {
                    holder.icon.setBackgroundResource(differ.currentList[position].icon)
                    holder.nameOption.text = differ.currentList[position].nameOption
                    if (differ.currentList[position].status) {
                        holder.viewChosen.setBackgroundResource(R.color.price)
                    } else {
                        holder.viewChosen.setBackgroundResource(R.color.transfer)
                    }
                }
            }
        }

    }

    private val diffCall = object : DiffUtil.ItemCallback<OptionDrawer>() {
        override fun areItemsTheSame(
            oldItem: OptionDrawer,
            newItem: OptionDrawer
        ): Boolean { //add and remove
            return oldItem.nameOption == newItem.nameOption
        }

        override fun areContentsTheSame(
            oldItem: OptionDrawer,
            newItem: OptionDrawer
        ): Boolean { // update
            return oldItem.status == newItem.status
        }

        override fun getChangePayload(
            oldItem: OptionDrawer,
            newItem: OptionDrawer
        ): Any? { //refresh 1 component in total
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