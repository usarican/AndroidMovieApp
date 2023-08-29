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
import com.example.mymovieapp.features.home.domain.model.CategoryType
import com.example.mymovieapp.utils.*
import com.example.mymovieapp.utils.extensions.dp
import kotlinx.coroutines.launch

class CategoryAdapter(
    private val lifeCycleScope : LifecycleCoroutineScope,
    private val clickListeners: CategoryMovieItemClickListeners,
    private val seeAllClickListener : MyClickListeners<CategoryType>
    ) : BaseAdapter<Category,CategoryItemBinding>(CATEGORY_ITEM_DIFF_UTIL) {
    override fun getResourceId(): Int = R.layout.category_item

    private lateinit var pagingLoadStateCallBack : PagingLoadStateCallBack

    override fun createHolderInstance(binding: CategoryItemBinding): BaseViewHolder {
        return object : BaseViewHolder(binding){
            override fun bind(model: Category) {
                val context = binding.root.context
                binding.category = model
                binding.categoryItemSeeAllButton.setOnClickListener {
                    seeAllClickListener.click(model.categoryType)
                }
                initializeRecyclerView(binding,context,model)
            }

        }
    }
    private fun initializeRecyclerView(binding: CategoryItemBinding,context : Context, category: Category){
        val adapter = CategoryMoviesAdapter()
        adapter.setClickListener(clickListeners)
        binding.categoryItemRecyclerView.layoutManager = LinearLayoutManager(context, HORIZONTAL,false)
        binding.categoryItemRecyclerView.adapter = adapter
        binding.categoryItemRecyclerView.addItemDecoration(
            EqualSpacingItemDecoration(
                8.dp,
                EqualSpacingItemDecoration.HORIZONTAL
            )
        )
        lifeCycleScope.launch {
            category.categoryItems?.let { adapter.submitData(it) }
        }
        HandlePagingLoadState(
            adapter = adapter,
            doOnLoading = { pagingLoadStateCallBack.doOnLoading(category.categoryType) },
            doOnSuccess = { pagingLoadStateCallBack.doOnSuccess(category.categoryType) },
            doOnError = { errorMessage -> pagingLoadStateCallBack.doOnError(errorMessage,category.categoryType) }
        )
    }

    fun setPagingLoadStateCallBack(pagingLoadStateCallBack: PagingLoadStateCallBack) {
        this.pagingLoadStateCallBack = pagingLoadStateCallBack
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