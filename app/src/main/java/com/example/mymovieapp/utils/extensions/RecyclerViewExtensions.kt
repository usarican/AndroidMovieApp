package com.example.mymovieapp.utils.extensions

import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.core.ui.BaseRecyclerAdapter

@BindingAdapter("setData")
fun setRecyclerViewData(recyclerView: RecyclerView,data : List<Any>?){
    val adapter = recyclerView.adapter
    if (adapter is ListAdapter<*,*> && data != null){
        adapter.submitList(data as List<Nothing>?)
    } else if (adapter is BaseRecyclerAdapter<*> && data != null){
        adapter.setData(data)
    }
}