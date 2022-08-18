package com.sejin.hyucafeteria.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sejin.hyucafeteria.data.Meal

class MealAdapter: ListAdapter<Meal, RecyclerView.ViewHolder>(MealDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

private class MealDiffCallback: DiffUtil.ItemCallback<Meal>(){
    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.menus == newItem.menus
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.menus == newItem.menus
    }
}