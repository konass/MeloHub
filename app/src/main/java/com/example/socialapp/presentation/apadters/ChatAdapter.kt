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
import com.example.socialapp.domain.models.ChatList
import kotlinx.android.synthetic.main.chat_item.view.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    inner class ChatViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    private val differCallback = object: DiffUtil.ItemCallback<ChatList>(){
        override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem.user?.userId==newItem.user?.userId
        }

        override fun areContentsTheSame(oldItem: ChatList, newItem:ChatList): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chat_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = differ.currentList[position]
        holder.itemView.apply{
            Glide.with(getContext()).load(chat.user?.imageUrl).into(im_man_c)
            tv_man_name_c.text=chat.user?.name
            Log.e("chatFragment", "success adapter" )
            tv_man_last_name_c.text= chat.user?.lastName
            tv_msg.text= chat.message?.message
            setOnClickListener{
                onItemClickListener?.let{
                    it(chat)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener: ((ChatList) -> Unit)? = null
    fun setOnItemClickListener (listener: (ChatList) -> Unit) {
        onItemClickListener=listener
    }

}