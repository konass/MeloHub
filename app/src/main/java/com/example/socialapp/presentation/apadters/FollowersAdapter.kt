package com.example.socialapp.presentation.apadters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.domain.models.Followers
import com.example.socialapp.domain.models.User
import kotlinx.android.synthetic.main.follow_item.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>(){
    inner class FollowersViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<Followers>(){
        override fun areItemsTheSame(oldItem: Followers, newItem: Followers): Boolean {
            return oldItem.user!!.userId ==newItem.user!!.userId
        }

        override fun areContentsTheSame(oldItem: Followers, newItem: Followers): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        return FollowersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.follow_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        val follower= differ.currentList[position]
        holder.itemView.apply{
            Glide.with(getContext()).load(follower.user?.imageUrl).into(im_man_f)
            tv_man_name_f.text=follower.user?.name
            tv_man_last_name_f.text= follower.user?.lastName
            setOnClickListener{
                onItemClickListener?.let{
                    follower.user?.let { it1 -> it(it1) }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((User) -> Unit)? = null
    fun setOnItemClickListener (listener: (User) -> Unit) {
        onItemClickListener=listener
    }

}