package com.example.mymovieapp.core.ui

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    abstract fun setData(list : List<Any>)
}