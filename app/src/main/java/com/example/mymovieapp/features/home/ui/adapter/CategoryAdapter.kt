package com.example.mymovieapp.features.home.ui.adapter

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseAdapter
import com.example.mymovieapp.databinding.CategoryItemBinding
import com.example.mymovieapp.features.home.domain.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryAdapter(private val lifeCycleScope : LifecycleCoroutineScope) : BaseAdapter<Category,CategoryItemBinding>(CATEGORY_ITEM_DIFF_UTIL) {
    override fun getResourceId(): Int = R.layout.category_item

    override fun createHolderInstance(binding: CategoryItemBinding): BaseViewHolder {
        return object : BaseViewHolder(binding){
            override fun bind(model: Category) {
                val context = binding.root.context
                binding.category = model
                initializeRecyclerView(binding,context,model)
            }

        }
    }
    private fun initializeRecyclerView(binding: CategoryItemBinding,context : Context, category: Category){
        val adapter = CategoryMoviesAdapter()
        binding.categoryItemRecyclerView.layoutManager = LinearLayoutManager(context, HORIZONTAL,false)
        binding.categoryItemRecyclerView.adapter = adapter
        lifeCycleScope.launch(Dispatchers.IO) {
            category.categoryItems?.let { adapter.submitData(it) }
        }
    }

    companion object {
        private val CATEGORY_ITEM_DIFF_UTIL = object : DiffUtil.ItemCallback<Category>(){
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
                oldItem.categoryType == newItem.categoryType

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
                oldItem.categoryType == newItem.categoryType

        }
    }
}