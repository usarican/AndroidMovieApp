package com.example.mymovieapp.features.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymovieapp.databinding.MovieDetailCommentItemBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetailReview
import com.example.mymovieapp.utils.extensions.whenReady

class MovieDetailCommentsAdapter : PagingDataAdapter<MovieDetailReview, MovieDetailCommentViewHolder>(
    COMMENT_DIFF_UTIL) {



    companion object {
        private val COMMENT_DIFF_UTIL = object : DiffUtil.ItemCallback<MovieDetailReview>() {
            override fun areItemsTheSame(
                oldItem: MovieDetailReview,
                newItem: MovieDetailReview
            ): Boolean {
                return oldItem.userName == newItem.userName
            }

            override fun areContentsTheSame(
                oldItem: MovieDetailReview,
                newItem: MovieDetailReview
            ): Boolean {
                return oldItem.userName == newItem.userName
            }

        }
    }

    override fun onBindViewHolder(holder: MovieDetailCommentViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieDetailCommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieDetailCommentItemBinding.inflate(layoutInflater,parent,false)
        return MovieDetailCommentViewHolder(binding)
    }
}

class MovieDetailCommentViewHolder(private val binding: MovieDetailCommentItemBinding) : ViewHolder(binding.root) {
    fun bind(movieDetailCommentItem : MovieDetailReview){
        binding.movieDetailComment = movieDetailCommentItem
        binding.authorComment.whenReady {
            binding.authorComment.performClick()
        }
    }
}
