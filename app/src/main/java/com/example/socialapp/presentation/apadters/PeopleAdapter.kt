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

import com.example.socialapp.domain.models.User
import kotlinx.android.synthetic.main.people_item.view.*


class PeopleAdapter : RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>(){
    inner class PeopleViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId==newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.people_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.itemView.apply{
            try{
                Glide.with(getContext()).load(user.imageUrl).into(im_man)
            }catch(e: NullPointerException){
                Log.e("MyTag", e.toString())
            }
            try{
                tv_man_name.text = user.name
                tv_man_last_name.text = user.lastName
            }catch(e: NullPointerException){
                Log.e("MyTag", e.toString())
            }
            setOnClickListener{
                onItemClickListener?.let{
                    it(user)
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