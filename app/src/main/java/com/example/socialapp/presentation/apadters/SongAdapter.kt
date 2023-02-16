package com.example.socialapp.presentation.apadters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.R
import com.example.socialapp.domain.models.Song
import kotlinx.android.synthetic.main.track_item.view.*


class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>(){
    inner class SongViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<Song>(){
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId==newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem:Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.track_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply{
            tv_track_name.text = song.title
            tv_artist.text = song.subtitle
            favorite_song.visibility=View.GONE
            btn_play.setOnClickListener{
                onItemClickListener?.let{
                    it(song)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }
    private var onItemClickListener: ((Song) -> Unit)? = null
    fun setOnItemClickListener (listener: (Song) -> Unit) {
        onItemClickListener=listener
    }

}