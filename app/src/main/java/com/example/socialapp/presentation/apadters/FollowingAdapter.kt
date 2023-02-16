package com.example.socialapp.presentation.apadters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.domain.models.Following
import com.example.socialapp.domain.models.User
import kotlinx.android.synthetic.main.follow_item.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>(){
    inner class FollowingViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<Following>(){
        override fun areItemsTheSame(oldItem: Following, newItem: Following): Boolean {
            return oldItem.user!!.userId ==newItem.user!!.userId
        }

        override fun areContentsTheSame(oldItem: Following, newItem: Following): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        return FollowingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.follow_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val follower= differ.currentList[position]
        holder.itemView.apply{
            Glide.with(getContext()).load(follower.user?.imageUrl).into(im_man_f)
            Log.e("FollowFragment", "success adapter" )
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