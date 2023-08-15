package com.example.mymovieapp.features.details.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.databinding.GenreListItemBinding

class BannerMovieGenreListAdapter(
) : RecyclerView.Adapter<BannerMovieGenreListViewHolder>() {

    private var genreList : List<String> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerMovieGenreListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GenreListItemBinding.inflate(layoutInflater,parent,false)
        return BannerMovieGenreListViewHolder(binding)
    }

    override fun getItemCount(): Int = genreList.size

    override fun onBindViewHolder(holder: BannerMovieGenreListViewHolder, position: Int) {
        val currentItem = genreList[position]
        holder.bind(currentItem)
    }

    fun setData(newList : List<String>){
        genreList = newList
        notifyDataSetChanged()
    }

}

class BannerMovieGenreListViewHolder(val binding: GenreListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(genre : String) {
        binding.genreItemChip.text = genre
    }
}