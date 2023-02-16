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
import com.example.socialapp.domain.models.Comment
import kotlinx.android.synthetic.main.comment_item.view.*


class CommentAdapter: RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem== newItem
        }
        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.comment_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:CommentAdapter.CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]
        holder.itemView.apply{

                Glide.with(getContext()).load(comment.author?.imageUrl).into(im_man_com)
                tv_man_name_com.text = comment.author?.name
            Log.e("CommentFragment", "success adapter" )
                tv_man_last_name_com.text= comment.author?.lastName
            tv_com.text= comment.text
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}