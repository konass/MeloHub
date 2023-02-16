package com.example.socialapp.presentation.chat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentMessagingBinding
import com.example.socialapp.domain.models.Message
import com.example.socialapp.domain.models.User
import com.example.socialapp.presentation.apadters.MessagingAdapter
import com.example.socialapp.utils.Prefs
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_messaging.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MessagingFragment : Fragment() {
    private var user: User? = null
    private lateinit var messageList: List<Message>
    private var  receiverUid: String= ""
    private val viewModel by viewModels<MessagingViewModel>()
    lateinit var binding: FragmentMessagingBinding
    private var senderUid: String= ""
    lateinit var senderRoom : String
    lateinit var messagingAdapter: MessagingAdapter
    lateinit var thisContext : Context
    private val bundleArgs by navArgs<MessagingFragmentArgs>()
    lateinit var receiverRoom : String
    private var message : Message?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       binding = FragmentMessagingBinding.inflate(layoutInflater,  container, false)
       return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        thisContext=context
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs= Prefs(thisContext)
        binding.imExit.setOnClickListener {
            view.findNavController().navigate(
                R.id.action_messagingFragment_to_chatFragment
            )
        }
        prefs.saveString("receiverId",bundleArgs.receiverUid.userId.toString())
        viewModel.getUser()
        lifecycleScope.launchWhenStarted {
            viewModel.getUserState.collect{
                when (it){
                    is Response.Success-> {
                        user= it.data
                        senderUid= user!!.userId
                        receiverUid= bundleArgs.receiverUid.userId
                        binding.tvManNameC.text=bundleArgs.receiverUid.name
                        binding.tvManLastNameC.text=bundleArgs.receiverUid.lastName
                        Glide.with(this@MessagingFragment).load(bundleArgs.receiverUid.imageUrl).into(binding.imManC)
                        senderRoom = senderUid + receiverUid
                        receiverRoom = receiverUid+senderUid
                        sentMessages(senderUid, senderRoom, receiverRoom, receiverUid,bundleArgs.receiverUid,
                            user!!)
                        getMessages(senderRoom)
                   }
                    is Response.Error -> {
                    }
                    is Response.Loading -> {
                    }
                }
            }
        }



    }
   private fun initMessageAdapter() {
       messagingAdapter = MessagingAdapter(thisContext,messageList, senderUid)
       binding.rvMessage.adapter=messagingAdapter
       binding.rvMessage.layoutManager=LinearLayoutManager(thisContext)
   }
    private fun getMessages(senderRoom: String) {
        viewModel.getMessages(senderRoom)
        lifecycleScope.launchWhenStarted {
            viewModel.getMessagesState.collect {
                when (it) {
                    is Response.Success -> {
                        messageList= it.data
                        initMessageAdapter()
                        messagingAdapter.notifyDataSetChanged()
                    }
                    is Response.Error -> {
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("MessagingFragment", "loading")
                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sentMessages (senderUid: String, senderRoom: String, receiverRoom: String, receiverUid: String, receiverUser: User, senderUser: User){
        btn_sent.setOnClickListener {
            message = Message(binding.messageBox.text.toString(), senderUid, receiverUid,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m:ss")) )
            viewModel.sentMessage(senderRoom, receiverRoom, message!!)
            lifecycleScope.launchWhenStarted {
                viewModel.setMessageState.collect{
                    when (it){
                        is Response.Success->{
                            Toast.makeText(activity, "Message was send", Toast.LENGTH_LONG).show()
                            binding.messageBox.setText("")
                            viewModel.saveChat(senderUid,
                                message!!, receiverUser, receiverUid)
                            viewModel.saveChat(receiverUid,
                                message!!, senderUser, senderUid)
                        }
                        is Response.Error -> {
                            Snackbar.make(
                                requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                            ).show()

                        }
                        is Response.Loading -> {
                            Log.e("MessagingFragment", "loading" )
                        }
                    }
                }
            }
        }
    }
}