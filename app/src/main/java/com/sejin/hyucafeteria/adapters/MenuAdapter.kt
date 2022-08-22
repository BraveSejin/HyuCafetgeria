package com.sejin.hyucafeteria.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.data.Meal
import com.sejin.hyucafeteria.data.Menu
import com.sejin.hyucafeteria.data.PageInfo
import com.sejin.hyucafeteria.databinding.ListItemMenuBinding
import com.sejin.hyucafeteria.utilities.isAlphabet
import com.sejin.hyucafeteria.utilities.reduceEngMenu

class MenuAdapter(private val pageInfo: PageInfo, private val meal: Meal) :
    ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ListItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), pageInfo, meal
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuViewHolder(
        private val binding: ListItemMenuBinding,
        private val pageInfo: PageInfo,
        private val meal: Meal
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Menu) {
            binding.apply {

                if (pageInfo.cafeteria.name == "학생식당") {
                    if (meal.title == "분식") {
                        val parenthesisEndIndex = item.name.indexOf(')')
                        menuContent.text = item.name.take(parenthesisEndIndex + 1)
                    } else {
                        menuContent.text =
                            item.name.split(" ").filter { str -> !str.first().isAlphabet() }
                                .joinToString(" ")
                    }
                } else if (pageInfo.cafeteria.name == "생과대" || pageInfo.cafeteria.name == "신소재") {

                    menuContent.text = item.name
                } else {
                    menuContent.text = item.name//.reduceEngMenu().replace(",", "\n")
                }



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