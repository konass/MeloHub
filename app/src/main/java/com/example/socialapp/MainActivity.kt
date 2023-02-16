package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

import com.example.socialapp.databinding.ActivityMainBinding
import com.example.socialapp.domain.models.Song
import com.example.socialapp.exoplayer.isPlaying
import com.example.socialapp.exoplayer.toSong
import com.example.socialapp.presentation.apadters.SwipeAdapter
import com.example.socialapp.presentation.music.MainMusicViewModel
import com.example.socialapp.presentation.post.PostActivity
import com.example.socialapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainMusicViewModel by viewModels()
    @Inject
    lateinit var swipeSongAdapter: SwipeAdapter
    private var curPlayingSong: Song? = null

    private var playbackState: PlaybackStateCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToObservers()
        bottom_nav.setupWithNavController(
            navController = nav_host_fragment.findNavController()
        )
        val userId=intent.getStringExtra("Username")
        addPost.setOnClickListener {
            val intent = Intent(this@MainActivity, PostActivity::class.java)
            intent.putExtra("UserId","$userId")
            startActivity(intent)
        }

        vpSong.adapter = swipeSongAdapter

        vpSong.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(playbackState?.isPlaying == true) {
                    mainViewModel.playOrToggleSong(swipeSongAdapter.songs[position])
                } else {
                    curPlayingSong = swipeSongAdapter.songs[position]
                }
            }
        })

        ivPlayPause.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

        swipeSongAdapter.setOnItemClickListener {
            nav_host_fragment.findNavController().navigate(
                R.id.globalActionToSongFragment
            )
        }

        nav_host_fragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.musicFragment -> showBottomBar()
                else -> hideBottomBar()
            }
        }
    }

    private fun hideBottomBar() {
        ivCurSongImage.isVisible = false
        vpSong.isVisible = false
        ivPlayPause.isVisible = false
    }

    private fun showBottomBar() {
        ivCurSongImage.isVisible = true
        vpSong.isVisible = true
        ivPlayPause.isVisible = true
    }

    private fun switchViewPagerToCurrentSong(song: Song) {
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if (newItemIndex != -1) {
            vpSong.currentItem = newItemIndex
            curPlayingSong = song
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { songs ->
                            swipeSongAdapter.songs = songs
                            if (songs.isNotEmpty()) {
                                Glide.with(this@MainActivity).load((curPlayingSong ?: songs[0]).imageUrl)
                                    .into(ivCurSongImage)
                            }
                            switchViewPagerToCurrentSong(curPlayingSong ?: return@observe)
                        }
                    }
                    Status.ERROR -> Unit
                    Status.LOADING -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(this) {
            if (it == null) return@observe

            curPlayingSong = it.toSong()
            Glide.with(this@MainActivity).load(curPlayingSong?.imageUrl).into(ivCurSongImage)
            switchViewPagerToCurrentSong(curPlayingSong ?: return@observe)
        }
        mainViewModel.playbackState.observe(this) {
            playbackState = it
            ivPlayPause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_b_pause else R.drawable.ic_btn_play_song
            )
        }
    }
}
