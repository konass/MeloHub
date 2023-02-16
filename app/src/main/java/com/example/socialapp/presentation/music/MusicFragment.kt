package com.example.socialapp.presentation.music

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.databinding.FragmentMusicBinding
import com.example.socialapp.domain.models.Song
import com.example.socialapp.presentation.apadters.ChartTrackAdapter
import com.example.socialapp.presentation.apadters.SearchTrackAdapter
import com.example.socialapp.presentation.apadters.SongAdapter
import com.example.socialapp.utils.Response
import com.example.socialapp.utils.Status
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_music.*

@AndroidEntryPoint
class MusicFragment : Fragment() {
    private val viewModel by viewModels<MusicViewModel>()
    private val mainViewModel by viewModels<MainMusicViewModel>()
    lateinit var chartTrackAdapter: ChartTrackAdapter
    lateinit var songAdapter: SongAdapter
    lateinit var binding: FragmentMusicBinding
    private lateinit var songList : List<Song>
    lateinit var searchTrackAdapter: SearchTrackAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentMusicBinding.inflate(layoutInflater,  container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMyMusic.visibility = View.VISIBLE
        binding.rvAllMusic.visibility = View.GONE
        binding.rvSearchTrackMusic.visibility = View.GONE
        initChartTrackAdapter()
        initSearchTrackAdapter()
        getFavoriteSong()
        subscribeToObservers()
        initSongAdapter()
        chartTrackAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
        chartTrackAdapter.setOnLikeClickListener {
            viewModel.saveSong(it)
        }
        binding.tabMusic.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rvMyMusic.visibility = View.VISIBLE
                        binding.rvAllMusic.visibility = View.GONE
                        getFavoriteSong()
                        songAdapter.notifyDataSetChanged()
                    }
                    1 -> {
                        // Toast.makeText(requireContext(), " All music", Toast.LENGTH_LONG).show()
                        //getChartTrack()
                        binding.rvMyMusic.visibility = View.GONE
                        binding.rvAllMusic.visibility = View.VISIBLE
                    }
                    else -> {
                        Toast.makeText(requireContext(), " no selected", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
//        binding.imSearch.setOnClickListener {
//            if ( binding.edSearchM.text!=null) {
//                binding.rvMyMusic.visibility = View.GONE
//                binding.rvAllMusic.visibility = View.GONE
//                binding.rvSearchTrackMusic.visibility = View.VISIBLE
//            }
//        }
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) : Boolean{
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val song = songAdapter.differ.currentList[position]
                viewModel.deleteSong(song)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).apply {
                    show()
                }

            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvMyMusic)
        songAdapter.notifyDataSetChanged()
    }

private fun initChartTrackAdapter() {
    chartTrackAdapter = ChartTrackAdapter()
    rv_all_music.apply {
        adapter = chartTrackAdapter
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}
    private fun initSongAdapter() {
      songAdapter = SongAdapter()
        rv_my_music.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
    private fun initSearchTrackAdapter(){
        searchTrackAdapter = SearchTrackAdapter()
        rv_searchTrack_music.apply {
            adapter= searchTrackAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Status.SUCCESS -> {
                   // allSongsProgressBar.isVisible = false
                    result.data?.let { songs ->
                        chartTrackAdapter.songs = songs
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> Unit
            }
        }
    }
    fun getFavoriteSong(){
       viewModel.getFavoriteSong()
       lifecycleScope.launchWhenStarted {
           viewModel.getFavoriteSongState.collect{
               when(it) {
                   is Response.Success ->{
                       songAdapter.differ.submitList(it.data)
                   }

                   is Response.Error -> {
                   }
                   is Response.Loading -> {
                   }
               }
           }
       }
    }
}
