package com.example.mymovieapp.features.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.databinding.CategoryMovieItemBinding
import com.example.mymovieapp.features.home.domain.model.Movie

class CategoryMoviesAdapter : PagingDataAdapter<Movie, CategoryMoviesViewHolder>(CATEGORY_MOVIE_ITEM_DIFF_UTIL) {
    companion object {
        private val CATEGORY_MOVIE_ITEM_DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>(){
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onBindViewHolder(holder: CategoryMoviesViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { movie ->
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMoviesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CategoryMovieItemBinding.inflate(layoutInflater,parent,false)
        return CategoryMoviesViewHolder(binding)
    }
}

class CategoryMoviesViewHolder(private val binding: CategoryMovieItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(model : Movie){
        binding.movie = model
    }
}