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
import com.example.socialapp.databinding.PostItemBinding
import com.example.socialapp.domain.models.Post
import com.example.socialapp.presentation.home.HomeViewModel
import kotlinx.android.synthetic.main.post_item.view.*


class PostAdapter (private val listener: HomeItemClickListener, val postViewModel: HomeViewModel):   RecyclerView.Adapter< PostAdapter.PostViewHolder>(){

   class PostViewHolder(val binding : PostItemBinding) : RecyclerView.ViewHolder(binding.root)


    private val differCallback = object: DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            val viewHolder = PostViewHolder(binding)
            viewHolder.binding.imLike.setOnClickListener {
                listener.onLikeClicked(differ.currentList[viewHolder.adapterPosition])

            }
            viewHolder.binding.imComment.setOnClickListener {
                listener.commentOnPost(differ.currentList[viewHolder.adapterPosition])
            }
            return viewHolder
        }
    var postId: String = ""
    lateinit var post : Post
        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            post =differ.currentList[position]
            postId = post.postId!!
          val  currentUserId = postViewModel.getAuthorLikeId()
        holder.itemView.apply {
            try {
                Glide.with(getContext()).load(post.imageUrl).into(im_post)
            } catch (e: NullPointerException) {
                Log.e("MyTag", e.toString())
            }
            try {
                tv_post.text = post.postText
            } catch (e: NullPointerException) {
                Log.e("MyTag", e.toString())
            }
            Glide.with(getContext()).load(post.author?.imageUrl).into(im_user)
            tv_name.text = post.author?.name
            tv_last_name.text = post.author?.lastName
                tv_nlike.text = post.likedBy?.size.toString()

                tv_ncomment.text = post.comments.size.toString()

            val isLiked = post.likedBy?.contains(currentUserId)
            if (isLiked == true) {
                holder.binding.imLike.setImageResource(
                    R.drawable.ic_unlike
                )
            } else {
                holder.binding.imLike.setImageResource(
                    R.drawable.ic_like
                )
            }

        }
    }



    private var onItemClickListener: ((Post) -> Unit)? = null
    fun setOnItemClickListener(listener: (Post) -> Unit) {
        onItemClickListener = listener
    }

    fun onLike(holder: PostViewHolder) {
        holder.itemView.im_like.visibility = View.GONE
        holder.itemView.im_unlike.visibility = View.VISIBLE
    }

    fun unLike(holder: PostViewHolder) {
        holder.itemView.im_like.visibility = View.VISIBLE
        holder.itemView.im_unlike.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
interface HomeItemClickListener {
    fun onLikeClicked(post: Post)
    fun commentOnPost(post: Post)
}