package com.example.socialapp.presentation.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.databinding.FragmentNotificationsBinding
import com.example.socialapp.domain.models.Notification
import com.example.socialapp.presentation.apadters.NotificationAdapter
import com.example.socialapp.presentation.profile.ProfileViewModel
import com.example.socialapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notifications.*


@AndroidEntryPoint
class NotificationsFragment : Fragment() {
 val viewModel by viewModels<NotificationViewModel>()
    val userViewModel by viewModels <ProfileViewModel>()
    var userId: String? = null
    lateinit var notificationAdapter : NotificationAdapter
    lateinit var binding : FragmentNotificationsBinding
   lateinit var notificationList:List<Notification>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotificationsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNotificationAdapter()
        getCurrentUser()
    }
    fun getCurrentUser(){
        userViewModel.getCurrentUser()
        lifecycleScope.launchWhenStarted {
            userViewModel.getUserState.collect{
                when(it){
                    is Response.Loading -> {
                    }
                    is Response.Success -> {
                        userId=it.data!!.userId
                        getAllNotification(userId!!)
                    }
                    is Response.Error-> {}
                }
            }
        }
    }
    fun getAllNotification(userId: String) {
        viewModel.getNotification(userId)
        lifecycleScope.launchWhenStarted {
            viewModel.getNotificationState.collect{
                when(it){
                    is Response.Loading -> {
                    }
                    is Response.Success -> {
                      notificationList= it.data
                        notificationAdapter.differ.submitList(notificationList)
                        notificationAdapter.notifyDataSetChanged()
                    }
                    is Response.Error-> {}
                }
            }
        }
    }
    fun initNotificationAdapter(){
        notificationAdapter = NotificationAdapter()
        rv_notification.apply {
            adapter = notificationAdapter
            layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
}