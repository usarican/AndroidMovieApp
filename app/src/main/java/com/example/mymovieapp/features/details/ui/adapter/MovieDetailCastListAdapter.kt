package com.example.mymovieapp.features.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.core.ui.BaseRecyclerAdapter
import com.example.mymovieapp.databinding.MovieDetailCastItemBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetailCast

class MovieDetailCastListAdapter : BaseRecyclerAdapter<MovieDetailCastViewHolder>() {

    private var movieDetailCastList : List<MovieDetailCast> = emptyList()

    override fun setData(list: List<Any>) {
        movieDetailCastList = list as List<MovieDetailCast>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailCastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieDetailCastItemBinding.inflate(layoutInflater,parent,false)
        return MovieDetailCastViewHolder(binding)
    }

    override fun getItemCount(): Int = movieDetailCastList.size

    override fun onBindViewHolder(holder: MovieDetailCastViewHolder, position: Int) {
        val currentItem = movieDetailCastList[position]
        currentItem?.let {
            holder.bind(currentItem)
        }
    }
}

class MovieDetailCastViewHolder(private val binding : MovieDetailCastItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item : MovieDetailCast){
        binding.movieDetailCastItem = item
    }
}