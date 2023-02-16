package com.example.socialapp.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.media.MediaBrowserServiceCompat
import com.example.socialapp.exoplayer.callbacks.MusicPlaybackPreparer
import com.example.socialapp.exoplayer.callbacks.MusicPlayerEventListener
import com.example.socialapp.exoplayer.callbacks.MusicPlayerNotificationListener
import com.example.socialapp.utils.Constants.MEDIA_ROOT_ID
import com.example.socialapp.utils.Constants.NETWORK_ERROR
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
private const val SERVICE_TAG = "MusicService"
@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {
    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory
    @Inject
    lateinit var exoPlayer: ExoPlayer
    @Inject
    lateinit var trackSource: TrackSource
    private lateinit var musicNotificationManager: MusicNotificationManager
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    var isForegroundService = false
    private var curPlayingSong: MediaMetadataCompat? = null
    private var isPlayerInitialized = false
    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    companion object {
        var curSongDuration = 0L
            private set
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
           trackSource.fetchMediaData()
        }
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken
        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {
            curSongDuration = exoPlayer.duration
        }
        val musicPlaybackPreparer = MusicPlaybackPreparer(trackSource) {
            curPlayingSong = it
            preparePlayer(
                trackSource.songs,
                it,
                true
            )
        }
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)
        exoPlayer.addListener(MusicPlayerEventListener(this))
        musicNotificationManager.showNotification(exoPlayer)
    }
    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return trackSource.songs[windowIndex].description
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSongIndex = if(curPlayingSong == null) 0 else songs.indexOf(itemToPlay)
       //exoPlayer.prepare()
        exoPlayer.prepare(trackSource.asMediaSource(dataSourceFactory))
         exoPlayer.seekTo(curSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(musicPlayerEventListener)
        exoPlayer.release()
        serviceScope.cancel()
    }
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot? {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {

        when(parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = trackSource.whenReady{ isInitialized ->
                    if(isInitialized) {
                        result.sendResult(trackSource.asMediaItems())
                        if(!isPlayerInitialized && trackSource.songs.isNotEmpty()) {
                            preparePlayer(trackSource.songs, trackSource.songs[0], false)
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if(!resultsSent) {
                    result.detach()
                }
            }
        }

          }

}