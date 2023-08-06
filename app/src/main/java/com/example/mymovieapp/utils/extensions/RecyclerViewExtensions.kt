package com.example.mymovieapp.utils.extensions

import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("setData")
fun setRecyclerViewData(recyclerView: RecyclerView,data : List<Any>?){
    val adapter = recyclerView.adapter
    if (adapter is ListAdapter<*,*> && data != null){
        adapter.submitList(data as List<Nothing>?)
    }
}