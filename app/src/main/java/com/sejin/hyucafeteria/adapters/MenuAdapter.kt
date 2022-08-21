package com.sejin.hyucafeteria.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.data.Menu
import com.sejin.hyucafeteria.databinding.ListItemMenuBinding
import com.sejin.hyucafeteria.utilities.reduceEngMenu

class MenuAdapter : ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ListItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuViewHolder(private val binding: ListItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Menu) {
            binding.apply {
                menuContent.text = item.name.reduceEngMenu()
                if (item.price.length > 2) {
                    menuPrice.text = item.price
                } else {
                    menuPrice.isGone = true
                }
            }
        }
    }
}

private class MenuDiffCallback : DiffUtil.ItemCallback<Menu>() {
    override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem.name == newItem.name
    }
}