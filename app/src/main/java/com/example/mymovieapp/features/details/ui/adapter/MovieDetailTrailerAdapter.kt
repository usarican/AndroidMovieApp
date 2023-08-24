package com.example.mymovieapp.features.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.lifecycle.Lifecycle
import com.example.mymovieapp.core.ui.BaseRecyclerAdapter
import com.example.mymovieapp.databinding.MovieDetailTrailerItemBinding
import com.example.mymovieapp.features.details.domain.model.MovieDetailVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback

class MovieDetailTrailerAdapter(
    private val lifeCycle : Lifecycle
): BaseRecyclerAdapter<MovieDetailTrailerAdapter.TrailerViewHolder>() {

    private var adapterList : List<MovieDetailVideo> = emptyList()

    inner class TrailerViewHolder(private val binding : MovieDetailTrailerItemBinding) : ViewHolder(binding.root) {
        fun bind(item : MovieDetailVideo) {
            binding.movieDetailTrailerItem = item

            lifeCycle.addObserver(binding.movieDetailTrailerWebView)
            binding.movieDetailTrailerWebView.getYouTubePlayerWhenReady(object :
                YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(
                        videoId = item.key,
                        startSeconds = 0f
                    )
                }
            })
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