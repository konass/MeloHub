package com.example.socialapp.presentation.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentChatBinding
import com.example.socialapp.domain.models.ChatList
import com.example.socialapp.domain.models.User
import com.example.socialapp.presentation.apadters.ChatAdapter

import com.example.socialapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var user: User? = null

    private val viewModel by viewModels<ChatViewModel>()
    private var senderUid: String= ""
    lateinit var chatAdapter: ChatAdapter
    lateinit var thisContext : Context
    lateinit var binding : FragmentChatBinding
    private lateinit var chatList: List<ChatList?>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentChatBinding.inflate(layoutInflater,  container, false)
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        thisContext=context
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChatAdapter()
        viewModel.getSenderUser()
        lifecycleScope.launchWhenStarted {
            viewModel.getSenderUserState.collect{
                when (it){
                    is Response.Success-> {
                        user= it.data
                        senderUid= user!!.userId
                        getChat(senderUid)
                    }
                    is Response.Error -> {
                    }
                    is Response.Loading -> {
                    }
                }
            }
        }
        chatAdapter.setOnItemClickListener {
            val bundle = bundleOf("receiverUid" to it.user)
            view.findNavController().navigate(
                R.id.action_chatFragment_to_messagingFragment,
                bundle
            )
        }
    }
private fun getChat(senderId: String){
    viewModel.getChat(senderId)
    lifecycleScope.launchWhenStarted {
        viewModel.getChatState.collect{
            when (it){
                is Response.Success-> {
                  chatList= it.data
                    chatAdapter.differ.submitList(chatList)
                }
                is Response.Error -> {
                }
                is Response.Loading -> {
                }
            }
        }
    }

}

    private fun initChatAdapter() {
        chatAdapter = ChatAdapter()
       rv_msg.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
}