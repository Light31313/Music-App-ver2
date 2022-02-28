package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.io.IOException

class MediaPlayerUtils {
    companion object {
        const val TAG = "MediaPlayerUtils"
    }

    private var mMediaPlayer = MediaPlayer()

    val mediaPlayer
        get() = mMediaPlayer

    val isLooping
        get() = mMediaPlayer.isLooping

    val isPlaying
        get() = mMediaPlayer.isPlaying

    fun setDataSource(context: Context, resId: Int) {
        mMediaPlayer = MediaPlayer.create(context, resId)
    }

    fun setDataSource(context: Context, uri: Uri) {
        try {
            mMediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            mMediaPlayer.setDataSource(context, uri)
            mMediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Log.d(TAG, "setDataSource: ${e.message}")
        }
    }

    fun setDataSource(url: String) {
        try {
            mMediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            mMediaPlayer.setDataSource(url)
            mMediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Log.d(TAG, "setDataSource: ${e.message}")

        }
    }


    fun start() {
        mMediaPlayer.start()
    }

    fun pause() {
        mMediaPlayer.pause()
    }

    fun stop() {
        mMediaPlayer.stop()
    }

    fun reset() {
        mMediaPlayer.reset()
    }

    fun getCurrentPosition(): Int =
        mMediaPlayer.currentPosition


    fun getDuration(): Int =
        mMediaPlayer.duration


    fun seekTo(milliseconds: Int) {
        if (milliseconds <= getDuration())
            mMediaPlayer.seekTo(milliseconds)
    }

    fun rewind(seconds: Int) {
        seekTo((getCurrentPosition() - seconds).coerceAtLeast(0))
    }

    fun fastForward(seconds: Int) {
        seekTo((getCurrentPosition() + seconds).coerceAtMost(getDuration()))

    }

    fun setLoop(isLoop: Boolean) {
        mMediaPlayer.isLooping = isLoop
    }

    fun release() {
        mMediaPlayer.release()
    }

}
