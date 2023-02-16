package com.example.socialapp.presentation.people

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.socialapp.databinding.FragmentPeopleBinding
import com.example.socialapp.domain.models.User
import com.example.socialapp.presentation.apadters.PeopleAdapter
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_people.*

@AndroidEntryPoint
class PeopleFragment : Fragment() {
    lateinit var peopleAdapter: PeopleAdapter
    private  lateinit var userList: List<User?>
    lateinit var userId: String
    private val viewModel by viewModels<PeopleViewModel>()
    lateinit var binding: FragmentPeopleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPeopleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserAdapter()
        getCurrentUser()
        binding.imSearch.setOnClickListener {
            if (binding.edSearch.text != null) {
                searchUserByName(binding.edSearch.text.toString())
                searchUserByLastName(binding.edSearch.text.toString())
            }
        }
        peopleAdapter.setOnItemClickListener {
            val bundle = bundleOf("user" to it)
            view.findNavController().navigate(
                R.id.action_peopleFragment_to_profilePeopleFragment,
                bundle
            )
        }
        lifecycleScope.launchWhenCreated {
            viewModel.getUserState.collect{
                when(it){
                    is Response.Success -> {
                        userList = it.data
                        peopleAdapter.differ.submitList(userList)
                        peopleAdapter.notifyDataSetChanged()
                    }
                    is Response.Error -> {
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is Response.Loading -> {
                        Log.e("PeopleFragment", "loading" )
                    }
                }

            }
        }
    }
    private fun initUserAdapter() {
        peopleAdapter = PeopleAdapter()
       rv_users.apply {
            adapter = peopleAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
    fun searchUserByName(name: String){
        lifecycleScope.launchWhenStarted {
           val list = viewModel.searchUserByName(name)
            peopleAdapter.differ.submitList(list)
        }
    }
    fun searchUserByLastName(lastName: String){
        lifecycleScope.launchWhenStarted {
            val list = viewModel.searchUserByLastName(lastName)
            peopleAdapter.differ.submitList(list)
        }
    }
    fun getCurrentUser(){
        viewModel.getCurrentUser()
        lifecycleScope.launchWhenStarted {
            viewModel.getCurrentUserState.collect{
                when(it){
                    is Response.Loading -> {}
                    is Response.Success ->{
                        userId= it.data!!.userId
                        viewModel.getAllUser(userId)
                    }
                    is Response.Error-> {}
                }
            }
        }
    }
}