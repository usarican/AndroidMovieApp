package com.example.mymovieapp.features.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovieapp.databinding.MovieDetailSimilarMovieItemBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetailSimilarMovie
import com.example.mymovieapp.utils.MyClickListeners

class MovieDetailSimilarMovieAdapter : PagingDataAdapter<MovieDetailSimilarMovie,MovieDetailSimilarMovieAdapter.SimilarMovieViewHolder>(
    SIMILAR_MOVIE_DIFF_UTIL) {

    private lateinit var myClickListener : MyClickListeners<Int>

    inner class SimilarMovieViewHolder(private val binding : MovieDetailSimilarMovieItemBinding) : ViewHolder(binding.root) {
        fun bind(item : MovieDetailSimilarMovie){
            binding.movie = item
            binding.root.setOnClickListener {
               myClickListener.click(item.id)
            }
        }
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieDetailSimilarMovieItemBinding.inflate(layoutInflater,parent,false)
        return SimilarMovieViewHolder(binding)
    }


    fun setItemClickListener(myClickListener: MyClickListeners<Int>) {
        this.myClickListener = myClickListener
    }
    companion object {
        private val SIMILAR_MOVIE_DIFF_UTIL = object : DiffUtil.ItemCallback<MovieDetailSimilarMovie>(){
            override fun areItemsTheSame(oldItem: MovieDetailSimilarMovie, newItem: MovieDetailSimilarMovie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieDetailSimilarMovie, newItem: MovieDetailSimilarMovie): Boolean {
                return oldItem.id == newItem.id            }
        }
    }
}