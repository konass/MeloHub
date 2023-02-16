package com.example.socialapp.presentation.apadters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.R
import com.example.socialapp.databinding.RvRecieverBinding
import com.example.socialapp.databinding.RvSenderBinding
import com.example.socialapp.domain.models.Message

class MessagingAdapter (var context : Context, var list : List<Message>, var uid: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
   var ITEM_SENT=1
    var ITEM_RECEIVE=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return if (viewType == ITEM_SENT)
    SentViewHolder(
        LayoutInflater.from(context).inflate(R.layout.rv_sender, parent, false)
    )
        else ReceiverViewHolder(
    LayoutInflater.from(context).inflate(R.layout.rv_reciever, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVE
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.itemViewType == ITEM_SENT) {
            val viewHolder = holder as  SentViewHolder
            viewHolder.binding.txtMessage.text= message.message
            viewHolder.binding.txtTime.text = message.time.toString()
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.txtMessage.text=message.message
            viewHolder.binding.txtTime.text = message.time.toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
 inner class SentViewHolder(view: View) : RecyclerView.ViewHolder(view){
     var binding= RvSenderBinding.bind(view)
 }
inner class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = RvRecieverBinding.bind(view)
}

}