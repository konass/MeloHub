package com.example.socialapp.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialapp.R
import com.example.socialapp.databinding.FragmentProfileBinding
import com.example.socialapp.domain.models.Post
import com.example.socialapp.domain.models.User
import com.example.socialapp.presentation.apadters.ProfileAdapter
import com.example.socialapp.presentation.apadters.ProfileItemClickListener
import com.example.socialapp.presentation.authentication.LoginActivity
import com.example.socialapp.utils.Prefs
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*


@AndroidEntryPoint
class ProfileFragment : Fragment(), ProfileItemClickListener {
    private var user: User? = null
    lateinit var thisContext : Context
    private val viewModel by viewModels<ProfileViewModel>()
    lateinit var postAdapter: ProfileAdapter
    private  lateinit var postList: List<Post?>

    lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater,  container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thisContext=context
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPostAdapter(this,viewModel)


        val prefs=Prefs(thisContext)
        binding.rvPostsUser.visibility=View.VISIBLE
        btn_ed_profile.setOnClickListener {
            val bundle = bundleOf("currentUser" to user)
            view.findNavController().navigate(
                R.id.action_profileFragment_to_profileEditFragment,
                bundle
            )
        }
        viewModel.getCurrentUser()
        lifecycleScope.launchWhenStarted {
            viewModel.getUserState.collect{
                when (it){
                    is Response.Success-> {
                        user= it.data
                        user?.userId?.let { it1 -> getUserPosts(it1) }
                        tv_name.text=user?.name.toString()
                        tv_last_name.text= user?.lastName.toString()
                        tv_desc.text=user?.description
                        Glide.with(this@ProfileFragment).load(user?.imageUrl).into(im_avatar)
                        binding.countOfFollowers.text= user?.nfollowers?.size.toString()
                        binding.countOfFollowing.text= user?.nfollowing?.size.toString()
                        binding.countOfFollowers.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("userIdToFollow", user!!.userId)
                            bundle.putString("type", "followers")
                            view.findNavController().navigate(
                                R.id.action_profileFragment_to_followFragment,
                                bundle
                            )
                        }
                        binding.countOfFollowing.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("userIdToFollow", user!!.userId)
                            bundle.putString("type", "following")
                            view.findNavController().navigate(
                                R.id.action_profileFragment_to_followFragment,
                                bundle
                            )
                        }
                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading" )
                    }
                }
            }
        }

        binding.tabProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rvPostsUser.visibility= View.VISIBLE
                    }
                    else -> {

                        Toast.makeText(requireContext(), " no selected",Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.imSignOut.setOnClickListener {
            viewModel.signOut()
            prefs.save("state",0)
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
    private fun initPostAdapter(listener: ProfileItemClickListener, viewModel: ProfileViewModel) {
        postAdapter = ProfileAdapter(listener, viewModel)
        binding.rvPostsUser.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getUserPosts(uid: String){
        viewModel.getPosts(uid)
        lifecycleScope.launchWhenStarted {
            viewModel.getPostState.collect{
                when (it){
                    is Response.Success->{
                        postList = it.data
                        postAdapter.differ.submitList(postList)
                        postAdapter.notifyDataSetChanged()

                    }
                    is Response.Error -> {

                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()

                    }
                    is Response.Loading -> {
                        Log.e("ProfileFragment", "loading" )
                    }
                }
            }
        }
    }

    override fun onLikeClicked(postId: String) {
        viewModel.updateLike(postId)
    }

    override fun commentOnPost(post: Post) {
        val bundle = bundleOf("post" to post.postId!!)
        view?.findNavController()?.navigate(
            R.id.action_profileFragment_to_commentFragment,
            bundle
        )
    }

    override fun deletePost(post: Post) {
        viewModel.deletePost(post.postId!!)
    }

}