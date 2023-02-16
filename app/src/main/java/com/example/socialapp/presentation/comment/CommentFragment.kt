package com.example.socialapp.presentation.comment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentCommentBinding
import com.example.socialapp.domain.models.Comment
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.domain.models.User
import com.example.socialapp.presentation.apadters.CommentAdapter
import com.example.socialapp.presentation.notifications.NotificationViewModel
import com.example.socialapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_comment.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CommentFragment : Fragment() {
    private val viewModel by viewModels<CommentViewModel>()
   lateinit var  commentList: List<Comment?>
    lateinit var comment : Comment
    var commentArray : ArrayList<Comment> = ArrayList()
    lateinit var commentAdapter: CommentAdapter
    lateinit var postId: String
    lateinit var binding : FragmentCommentBinding
    private val notificationViewModel by viewModels<NotificationViewModel>()
    private var notification : Notification? = null
    private val bundleArgs by navArgs<CommentFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCommentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = bundleArgs.post.postId!!
            getAuthor()
        getComment(postId)
        binding.imExitComment.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_commentFragment_to_homeFragment
            )
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAuthor (){
        viewModel.getUser()
        lifecycleScope.launchWhenStarted {
            viewModel.getUserState.collect{
                when (it) {
                    is Response.Success-> {
                     setComment(postId, it.data)
                    }
                    is Response.Loading->{

                    }
                    is Response.Error->{

                    }
                }
            }
        }
    }
    private fun getComment(postId:String){
        initCommentAdapter()
        viewModel.getComment(postId)
        lifecycleScope.launchWhenStarted {
            viewModel.getCommentState.collect{
                when (it) {
                    is Response.Success-> {
                        commentList = it.data
                        commentAdapter.differ.submitList(commentList)
                        commentAdapter.notifyDataSetChanged()

                    }
                    is Response.Loading->{

                    }
                    is Response.Error->{

                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setComment (postId: String, author: User?) {
        binding.btnSentCom.setOnClickListener {
            comment =
                Comment(author, binding.commentBox.text.toString(), LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("H:m:ss")))
            viewModel.setComment(postId, comment)
            lifecycleScope.launchWhenStarted {
                viewModel.setCommentState.collect {
                    when (it) {
                        is Response.Success -> {
                            binding.commentBox.setText("")
                            commentArray.add(comment)
                           viewModel.updateComment(postId, author!!.userId)
                            setNotification("${author.name} ${author.lastName} commented on your post ${ binding.commentBox.text} at ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m:ss"))}",  bundleArgs.post.userId!! )
                        }
                        is Response.Loading -> {

                        }
                        is Response.Error -> {

                        }
                    }
                }
            }
        }
    }
    private fun initCommentAdapter(){
        commentAdapter = CommentAdapter()
      rv_comment.apply {
            adapter= commentAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
fun setNotification( text: String, receiverId: String){
 notification = Notification (text)
notificationViewModel.setNotification(receiverId, notification!!)
}
}