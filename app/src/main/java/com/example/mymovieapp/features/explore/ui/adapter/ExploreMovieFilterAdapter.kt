package com.example.mymovieapp.features.explore.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseAdapter
import com.example.mymovieapp.databinding.FilterDialogFilterItemBinding
import com.example.mymovieapp.features.explore.ui.dialog.MovieFilterDialogItem
import com.example.mymovieapp.utils.MyClickListeners


class ExploreMovieFilterAdapter(
    private val myClickListeners: MyClickListeners<MovieFilterDialogItem>
) : ListAdapter<MovieFilterDialogItem,ExploreMovieFilterAdapter.FilterItemViewHolder>(
    FILTER_DIALOG_DIFF_UTILS) {


    inner class FilterItemViewHolder(private val binding: FilterDialogFilterItemBinding) : ViewHolder(binding.root){
        fun bind(model : MovieFilterDialogItem, myClickListeners: MyClickListeners<MovieFilterDialogItem>) {
            binding.filterDialogItem = model
            binding.myClickListener = myClickListeners
            val context = binding.root.context
            binding.apply {
                if (model.isItemSelected) {
                    filterDialogItemContainer.setCardBackgroundColor(ColorStateList.valueOf(context.resources.getColor(R.color.primary_color)))
                    filterDialogItemText.setTextColor(context.resources.getColor(R.color.white))
                } else {
                    filterDialogItemContainer.setCardBackgroundColor(ColorStateList.valueOf(context.resources.getColor(R.color.white)))
                    filterDialogItemText.setTextColor(context.resources.getColor(R.color.primary_color))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FilterDialogFilterItemBinding.inflate(layoutInflater,parent,false)
        return FilterItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterItemViewHolder, position: Int) {
        holder.bind(currentList[position],myClickListeners)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object {
        private val FILTER_DIALOG_DIFF_UTILS = object : DiffUtil.ItemCallback<MovieFilterDialogItem>() {
            override fun areItemsTheSame(
                oldItem: MovieFilterDialogItem,
                newItem: MovieFilterDialogItem
            ): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.itemCategory == newItem.itemCategory
            }

            override fun areContentsTheSame(
                oldItem: MovieFilterDialogItem,
                newItem: MovieFilterDialogItem
            ): Boolean {
                return oldItem.isItemSelected == newItem.isItemSelected
            }
        }
    }
}