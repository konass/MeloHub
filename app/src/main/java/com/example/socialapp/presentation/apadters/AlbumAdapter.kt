package com.example.socialapp.presentation.apadters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.data.remote.dto.Album

import kotlinx.android.synthetic.main.music_item.view.*
import kotlinx.android.synthetic.main.post_item.view.*


class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>(){
    inner class AlbumViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.mbid==newItem.mbid
        }

        override fun areContentsTheSame(oldItem: Album, newItem:Album): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AlbumViewHolder {
        return AlbumViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.music_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album= differ.currentList[position]
        holder.itemView.apply{
            Glide.with(getContext()).load(album.image).into(im_user)
            tv_name_m.text =album.name
            setOnClickListener{
                onItemClickListener?.let{
                    it(album)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((Album) -> Unit)? = null
    fun setOnItemClickListener (listener: (Album) -> Unit) {
        onItemClickListener=listener
    }

}