package com.example.socialapp.presentation.apadters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.data.remote.dto.Track
import kotlinx.android.synthetic.main.music_item.view.*


class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(){
    inner class TrackViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<Track>(){
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.mbid==newItem.mbid
        }

        override fun areContentsTheSame(oldItem: Track, newItem:Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.music_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track= differ.currentList[position]
        holder.itemView.apply{
            Glide.with(getContext()).load(track.image).into(im_music)
            tv_name_m.text =track.name
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
    private var onItemClickListener: ((Track) -> Unit)? = null
    fun setOnItemClickListener (listener: (Track) -> Unit) {
        onItemClickListener=listener
    }

}