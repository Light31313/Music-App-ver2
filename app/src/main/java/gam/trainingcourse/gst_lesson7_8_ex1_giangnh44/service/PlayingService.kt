package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service


import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat

import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.MyApplication
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity.Song
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.receiver.PlayingReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlayingService : Service() {

    val ACTION_PREVIOUS = 1

    val ACTION_PLAY = 2
    val ACTION_NEXT = 3

    val isPlaying = MutableLiveData(true)
    val clickNextOrPrev = MutableLiveData(true)


    val songs = arrayListOf<Song>()
    var currentPosition = 0

    private val playingBinder = PlayingBinder()


    inner class PlayingBinder : Binder() {
        fun getPlayingService() = this@PlayingService
    }

    override fun onBind(p0: Intent?): IBinder {
        return playingBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // check if service get intent from SongListFragment or PlayingReceiver
        if (intent?.getIntExtra("position", -1) != -1) {

            val bundle = intent?.getBundleExtra("bundle")
            // get all songs data for the first time run into onStartCommand
            if (bundle != null) {

                songs.addAll(bundle.getSerializable("songs") as ArrayList<Song>)
            }
            currentPosition = intent!!.getIntExtra("position", 0)

            sendNotificationMedia(songs[currentPosition])

            return START_NOT_STICKY
        }
        // if intent from PlayingReceiver get action from notification button
        val action = intent.getIntExtra("action", 0)

        handleAction(action)

        return START_NOT_STICKY
    }


    private fun sendNotificationMedia(song: Song) {
        CoroutineScope(Dispatchers.IO).launch {
            // load song image into Notification
            Glide.with(this@PlayingService).asBitmap().load(song.songImage)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val mediaSessionCompat = MediaSessionCompat(this@PlayingService, "tag")

                        val notification =
                            NotificationCompat.Builder(
                                this@PlayingService,
                                MyApplication.CHANNEL_ID
                            )
                                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                                .setSubText("GiangNH44")
                                .setContentTitle(song.name)
                                .setContentText(song.author)
                                .setLargeIcon(resource)
                                .setPriority(NotificationCompat.PRIORITY_LOW)
                                .addAction(
                                    R.drawable.ic_baseline_skip_previous_24,
                                    "Previous",
                                    getPendingIntent(ACTION_PREVIOUS)
                                )
                                .addAction(
                                    if (isPlaying.value == true) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24,
                                    if (isPlaying.value == true) "Pause" else "Start",
                                    getPendingIntent(ACTION_PLAY)
                                )
                                .addAction(
                                    R.drawable.ic_baseline_skip_next_24,
                                    "Next",
                                    getPendingIntent(ACTION_NEXT)
                                )
                                .setStyle(
                                    androidx.media.app.NotificationCompat.MediaStyle()
                                        .setShowActionsInCompactView(1, 2)
                                        .setMediaSession(mediaSessionCompat.sessionToken)
                                )
                                .build()

                        startForeground(1, notification)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

    }

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

    fun playSong() {
        //update UI of Playing Fragment
        isPlaying.value = isPlaying.value != true
        //update UI of Notification
        sendNotificationMedia(songs[currentPosition])
    }


    fun nextSong() {
        currentPosition++
        if (currentPosition > songs.size - 1)
            currentPosition = 0
        //update UI of Notification
        sendNotificationMedia(songs[currentPosition])
        //update UI of Playing Fragment
        clickNextOrPrev.value = true
    }

    fun prevSong() {
        currentPosition--
        if (currentPosition < 0)
            currentPosition = songs.size - 1
        //update UI of Notification
        sendNotificationMedia(songs[currentPosition])
        //update UI of Playing Fragment
        clickNextOrPrev.value = true
    }

    fun clickSeekBar() {
        Toast.makeText(this, "Change Seek Bar", Toast.LENGTH_SHORT).show()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }


}