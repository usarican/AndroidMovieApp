package com.example.mymovieapp.features.home.ui.adapter


import androidx.recyclerview.widget.DiffUtil
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseAdapter
import com.example.mymovieapp.databinding.BannerMovieItemBinding
import com.example.mymovieapp.features.home.domain.model.Movie


class BannerMoviesAdapter() : BaseAdapter<Movie,BannerMovieItemBinding>(BANNER_MOVIE_DIFF_UTIL_CALLBACK) {
    override fun getResourceId(): Int = R.layout.banner_movie_item

    override fun createHolderInstance(binding: BannerMovieItemBinding): BaseViewHolder {
        return object : BaseViewHolder(binding){
            override fun bind(model: Movie) {
                binding.movie = model
                binding.executePendingBindings()
            }

        }
    }

    companion object {
        private val BANNER_MOVIE_DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Movie>(){
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}
