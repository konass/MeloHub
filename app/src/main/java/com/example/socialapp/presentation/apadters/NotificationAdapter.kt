package com.example.socialapp.presentation.apadters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.R
import com.example.socialapp.domain.models.Notification
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem== newItem
        }
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.notification_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:NotificationAdapter.NotificationViewHolder, position: Int) {
        val notification = differ.currentList[position]
        holder.itemView.apply{
       tv_notification.text= notification.text
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}