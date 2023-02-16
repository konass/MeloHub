package com.example.socialapp.presentation.apadters

import com.example.socialapp.data.remote.dto.TrackX


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.R
import kotlinx.android.synthetic.main.music_item.view.*



class SearchTrackAdapter : RecyclerView.Adapter<SearchTrackAdapter.SearchTrackViewHolder>(){
    inner class SearchTrackViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<TrackX>(){
        override fun areItemsTheSame(oldItem: TrackX, newItem: TrackX): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: TrackX, newItem:TrackX): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        return SearchTrackViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.music_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        val track = differ.currentList[position]
        holder.itemView.apply{
          tv_name_m.text= track.name
            Glide.with(getContext()).load(track.image).into(im_music)
            setOnClickListener{
                onItemClickListener?.let{
                    it(track)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((TrackX) -> Unit)? = null
    fun setOnItemClickListener (listener: (TrackX) -> Unit) {
        onItemClickListener=listener
    }

}