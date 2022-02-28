package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service


import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadata
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.MyApplication
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model.Song
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.broadcastreceiver.PlayingReceiver
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.MusicManager


class PlayingService : Service() {
    companion object {
        const val TAG = "PlayingService"

        const val ACTION = "action"
        const val ACTION_PREVIOUS = 0
        const val ACTION_PLAY = 1
        const val ACTION_NEXT = 2
        const val FOREGROUND_SERVICE_ID = 1
    }


    private val playingBinder = PlayingBinder()

    override fun onCreate() {
        super.onCreate()
        MusicManager.mediaPlayer.setOnCompletionListener {
            if (MusicManager.mode == MusicManager.NORMAL)
                nextSong()
            else if (MusicManager.mode == MusicManager.SHUFFLE)
                randomSong()
        }
    }

    inner class PlayingBinder : Binder() {
        fun getPlayingService() = this@PlayingService
    }

    override fun onBind(p0: Intent?): IBinder {
        return playingBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // if intent from PlayingReceiver get action from notification button
        val action = intent?.getIntExtra(ACTION, -1)

        if (action == -1) {
            MusicManager.songs.value?.let {
                MusicManager.startSong()
                sendNotificationMedia(it[MusicManager.currentPlayingPosition])
            }

        } else {
            action?.let { handleAction(it) }
        }

        return START_NOT_STICKY
    }


    private fun sendNotificationMedia(song: Song) {
        // load song image into Notification
        Glide.with(this@PlayingService).asBitmap().load(song.songImage)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val mediaSessionCompat = MediaSessionCompat(this@PlayingService, TAG).apply {
                        setMetadata(MediaMetadataCompat.Builder()
                            .putString(MediaMetadata.METADATA_KEY_TITLE, song.songName)
                            .putString(MediaMetadata.METADATA_KEY_ARTIST, song.singerName)
                            .build())
                    }

                    val notification =
                        NotificationCompat.Builder(
                            this@PlayingService,
                            MyApplication.CHANNEL_ID
                        )
                            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                            .setSubText("GiangNH44")
                            .setContentTitle(song.songName)
                            .setContentText(song.singerName)
                            .setLargeIcon(resource)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .addAction(
                                R.drawable.ic_baseline_skip_previous_24,
                                "Previous",
                                getPendingIntent(ACTION_PREVIOUS)
                            )
                            .addAction(
                                if (MusicManager.isPlaying.value != false) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24,
                                if (MusicManager.isPlaying.value != false) "Pause" else "Start",
                                getPendingIntent(ACTION_PLAY)
                            )
                            .addAction(
                                R.drawable.ic_baseline_skip_next_24,
                                "Next",
                                getPendingIntent(ACTION_NEXT)
                            )
                            .setStyle(
                                androidx.media.app.NotificationCompat.MediaStyle()
                                    .setShowActionsInCompactView(ACTION_PLAY, ACTION_NEXT)
                                    .setMediaSession(mediaSessionCompat.sessionToken)
                            )
                            .build()
                    startForeground(FOREGROUND_SERVICE_ID, notification)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })


    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(action: Int): PendingIntent {
        val intent = Intent(this, PlayingReceiver::class.java)
        intent.putExtra("action", action)

        return PendingIntent.getBroadcast(
            applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun handleAction(action: Int) {
        when (action) {
            ACTION_PREVIOUS -> prevSong()
            ACTION_PLAY -> playSong()
            ACTION_NEXT -> nextSong()
            else -> Toast.makeText(this, "Wrong action", Toast.LENGTH_SHORT).show()
        }
    }

    fun prevSong() {
        MusicManager.previousSong()
        MusicManager.songs.value?.let {
            sendNotificationMedia(it[MusicManager.currentPlayingPosition])
        }

    }

    fun nextSong() {
        MusicManager.nextSong()
        MusicManager.songs.value?.let {
            sendNotificationMedia(it[MusicManager.currentPlayingPosition])
        }
    }

    fun playSong() {
        MusicManager.playMusic()
        MusicManager.songs.value?.let {
            sendNotificationMedia(it[MusicManager.currentPlayingPosition])
        }
    }

    fun randomSong() {
        MusicManager.startRandomSong()
        MusicManager.songs.value?.let {
            sendNotificationMedia(it[MusicManager.currentPlayingPosition])
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }


}