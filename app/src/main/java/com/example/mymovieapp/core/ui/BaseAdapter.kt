package com.example.mymovieapp.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.features.home.domain.model.Movie

abstract class BaseAdapter<T : Any, VDB : ViewDataBinding>(
    private val diffUtilCallBack: DiffUtil.ItemCallback<T>
) : ListAdapter<T,BaseAdapter<T,VDB>.BaseViewHolder>(diffUtilCallBack) {

    abstract inner class BaseViewHolder(private val binding: VDB) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(model : T)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val currentItem = currentList[position]
        currentItem?.let { item ->
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<VDB>(layoutInflater,getResourceId(),parent,false)
        return createHolderInstance(binding)
    }

    @LayoutRes
    abstract fun getResourceId() : Int

    abstract fun createHolderInstance(binding: VDB) : BaseViewHolder


}

class DiffUtilCallBack<T : Any> : DiffUtil.ItemCallback<T>(){
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem as Movie) == newItem
    }

}