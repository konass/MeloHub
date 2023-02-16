package com.example.socialapp.presentation.apadters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.data.remote.dto.Artist

import kotlinx.android.synthetic.main.music_item.view.*



class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>(){
    inner class ArtistViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<Artist>(){
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.mbid==newItem.mbid
        }

        override fun areContentsTheSame(oldItem: Artist, newItem:Artist): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ArtistViewHolder {
        return ArtistViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.music_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist= differ.currentList[position]
        holder.itemView.apply{
            Glide.with(getContext()).load(artist.image).into(im_music)
            tv_name_m.text =artist.name
            setOnClickListener{
                onItemClickListener?.let{
                    it(artist)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((Artist) -> Unit)? = null
    fun setOnItemClickListener (listener: (Artist) -> Unit) {
        onItemClickListener=listener
    }

}