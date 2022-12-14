package com.sejin.hyucafeteria.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.data.Meal
import com.sejin.hyucafeteria.data.PageInfo
import com.sejin.hyucafeteria.databinding.ListItemMealBinding
import com.sejin.hyucafeteria.databinding.ListItemMenuBinding

class MealAdapter(private val pageInfo: PageInfo) :
    ListAdapter<Meal, MealAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            ListItemMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), pageInfo
        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(private val binding: ListItemMealBinding, private val pageInfo: PageInfo) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Meal) {

            binding.menuTitle.text = item.title
            binding.menuRcv.adapter = MenuAdapter(pageInfo, item).apply {
                submitList(item.menus)
            }
        }
    }
}

private class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.menus == newItem.menus
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.menus == newItem.menus
    }
}