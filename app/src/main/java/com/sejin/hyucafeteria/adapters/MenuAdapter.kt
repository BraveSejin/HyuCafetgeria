package com.sejin.hyucafeteria.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.data.Meal
import com.sejin.hyucafeteria.data.Menu
import com.sejin.hyucafeteria.data.PageInfo
import com.sejin.hyucafeteria.databinding.ListItemMenuBinding
import com.sejin.hyucafeteria.utilities.*

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
//            bindPrice(item)
            bindMenuName(item)
        }

        private fun bindPrice(item: Menu) {
            if (item.price.length > 2) {
                binding.menuPrice.text = item.price
            } else {
                disablePrice()
            }
        }

        private fun disablePrice() {
            binding.menuPrice.isGone = true
        }

        private fun bindMenuName(item: Menu) {
            binding.menuContent.text = when (pageInfo.cafeteria.name) {
                "학생식당" -> {
                    if (meal.title == "분식")
//                        item.getMenuNameUntilParenthesisEnd()
                        item.getMenuNameBeforeParenthesisStart()
                    else if (item.isSquareBracketMenu()) {
                        val pair = item.getSquareBracketMenuName()
                        binding.menuBracket.isVisible = true
                        binding.menuBracket.text = pair.first
                        pair.second.removeEngWords()
                    } else item.getMenuNameWithoutEngWords()
                }
                else -> {
                    if (item.isSquareBracketMenu()) {
                        val pair = item.getSquareBracketMenuName()
                        binding.menuBracket.isVisible = true
                        binding.menuBracket.text = pair.first
                        pair.second
                    } else {
                        item.name
                    }
                }
            }.commaToNewLine()
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