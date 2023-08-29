package com.example.mymovieapp.features.explore.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovieapp.databinding.ExploreMovieItemBinding
import com.example.mymovieapp.features.home.domain.model.Movie
import com.example.mymovieapp.utils.MyClickListeners

class ExploreMoviePagingAdapter(
    private val clickListeners: MyClickListeners<Int>
) : PagingDataAdapter<Movie,ExploreMoviePagingAdapter.ExploreMovieViewHolder>(
    EXPLORE_MOVIE_DIFF_UTIL) {

    inner class ExploreMovieViewHolder(private val binding : ExploreMovieItemBinding) : ViewHolder(binding.root) {
        fun bind(item : Movie) {
            binding.movie = item
            binding.root.setOnClickListener {
                clickListeners.click(item.id)
            }
        }
    }

    companion object {
        private val EXPLORE_MOVIE_DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>(){
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: ExploreMovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreMovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ExploreMovieItemBinding.inflate(layoutInflater,parent,false)
        return ExploreMovieViewHolder(binding)
    }
}