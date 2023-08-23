package com.example.mymovieapp.features.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovieapp.core.ui.BaseRecyclerAdapter
import com.example.mymovieapp.databinding.MovieDetailTrailerItemBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetailVideo

class MovieDetailTrailerAdapter : BaseRecyclerAdapter<MovieDetailTrailerAdapter.TrailerViewHolder>() {

    private var adapterList : List<MovieDetailVideo> = emptyList()

    inner class TrailerViewHolder(private val binding : MovieDetailTrailerItemBinding) : ViewHolder(binding.root) {
        fun bind(item : MovieDetailVideo) {
            binding.movieDetailTrailerItem = item
        }
    }

    override fun setData(list: List<Any>) {
        adapterList = list as List<MovieDetailVideo>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieDetailTrailerItemBinding.inflate(layoutInflater,parent,false)
        return TrailerViewHolder(binding)
    }

    override fun getItemCount(): Int = adapterList.size

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val currentItem = adapterList[position]
        holder.bind(currentItem)
    }
}