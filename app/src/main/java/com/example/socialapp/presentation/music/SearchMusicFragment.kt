package com.example.socialapp.presentation.music

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.data.remote.dto.Artist
import com.example.socialapp.data.remote.dto.Track
import com.example.socialapp.databinding.FragmentSearchMusicBinding
import com.example.socialapp.presentation.apadters.ArtistAdapter
import com.example.socialapp.presentation.apadters.SearchTrackAdapter
import com.example.socialapp.presentation.apadters.TrackAdapter
import com.example.socialapp.utils.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_music.*

@AndroidEntryPoint
class SearchMusicFragment : Fragment() {
private val viewModel by viewModels<MusicViewModel>()
    lateinit var binding : FragmentSearchMusicBinding
    private lateinit var trackList : List<Track>
    private lateinit var artistList: List<Artist>
    lateinit var searchTrackAdapter: SearchTrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var artistAdapter: ArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding=FragmentSearchMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArtistAdapter()
        initTrackAdapter()
        initSearchTrackAdapter()
        getChartTrack()
        getArtist()
        rv_artists.visibility=View.VISIBLE
        rv_tracks.visibility=View.VISIBLE
        rv_search_tracks.visibility=View.GONE
        rv_search_artists.visibility=View.GONE
        binding.imSearchMusic.setOnClickListener {
            if (binding.edSearchMusic.text != null) {
                searchTrack(binding.edSearchMusic.text.toString())
                searchArtist(binding.edSearchMusic.text.toString())
            }
        }
        artistAdapter.setOnItemClickListener { artist ->
        goToWebSite(artist.url)
        }
        trackAdapter.setOnItemClickListener { track->
            track.url?.let { goToWebSite(it) }
        }
        searchTrackAdapter.setOnItemClickListener {
            it.url?.let { it1 -> goToWebSite(it1) }
        }

    }

    fun getChartTrack(){
    viewModel.getChartTrack()
    lifecycleScope.launchWhenStarted {
        viewModel.getChartTrackState.collect{
            when(it){
                is Response.Success -> {
                 trackList = it.data!!
                 trackAdapter.differ.submitList(trackList)
                }
                is Response.Loading -> {

                }
                is Response.Error ->{
                    Snackbar.make(
                        requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
    fun getArtist(){
        viewModel.getTopArtists()
        lifecycleScope.launchWhenStarted {
            viewModel.getTopArtistsState.collect{
                when(it){
                    is Response.Success -> {
                        artistList = it.data!!
                        artistAdapter.differ.submitList(artistList)
                    }
                    is Response.Loading -> {

                    }
                    is Response.Error ->{
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
    fun searchTrack(trackName: String?) {
        viewModel.getSearchTrack(trackName)
        lifecycleScope.launchWhenStarted {
            viewModel.getSearchTrackState.collect{
                when(it) {
                    is Response.Success -> {
                        searchTrackAdapter.differ.submitList(it.data!!.results.trackmatches.track)
                        rv_tracks.visibility=View.GONE
                        rv_search_tracks.visibility=View.VISIBLE
                    }
                    is Response.Loading ->{

                    }
                    is Response.Error -> {
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }
    }
    fun searchArtist(artistName: String?) {
        viewModel.getSearchArtists(artistName)
        lifecycleScope.launchWhenStarted {
            viewModel.getSearchArtistState.collect{
                when(it) {
                    is Response.Success -> {
                        artistAdapter.differ.submitList(it.data)

                    }
                    is Response.Loading ->{

                    }
                    is Response.Error -> {
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }
    }
    private fun initArtistAdapter() {
        artistAdapter = ArtistAdapter()
        rv_artists.apply {
            adapter = artistAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initTrackAdapter() {
        trackAdapter = TrackAdapter()
        rv_tracks.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    private fun initSearchTrackAdapter(){
        searchTrackAdapter = SearchTrackAdapter()
        rv_search_tracks.apply {
            adapter= searchTrackAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    fun goToWebSite(url: String) {
        try {
            Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(takeIf {
                    URLUtil.isValidUrl(url)
                }?.let {
                    url
                } ?: "https://google.com"))
                .let {
                    ContextCompat.startActivity(requireContext(), it, null)
                }
        } catch (e:Exception){
            Toast.makeText(context, "Thr device doesn't have any browser to vise the document", Toast.LENGTH_SHORT)

        }
    }
}