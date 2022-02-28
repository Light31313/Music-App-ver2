package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model.Song
import kotlin.random.Random

object MusicManager {
    const val NORMAL = 0
    const val LOOP = 1
    const val SHUFFLE = 2

    var mode = NORMAL

    private val mSongs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>>
        get() = mSongs

    fun setSongs(songs: List<Song>) {
        mSongs.value = songs
    }

    private val mPlayingSong = MutableLiveData<Song>()
    val playingSong: LiveData<Song>
        get() = mPlayingSong

    val mediaPlayer
        get() = mediaPlayerUtils.mediaPlayer

    fun setPlayingSong(song: Song) {
        mPlayingSong.value = song
    }

    var currentPlayingPosition = 0

    private val mediaPlayerUtils = MediaPlayerUtils()

    private val mIsPrepared = MutableLiveData<Boolean>()
    val isPrepared: LiveData<Boolean>
        get() = mIsPrepared

    private val mIsPlaying = MutableLiveData(mediaPlayerUtils.isPlaying)
    val isPlaying: LiveData<Boolean>
        get() = mIsPlaying

    val isLooping
        get() = mediaPlayerUtils.isLooping

    init {
        mediaPlayer.setOnPreparedListener {
            playMusic()
            mIsPrepared.value = true
            mIsPlaying.value = true
        }

    }

    fun startSong() {
        mediaPlayerUtils.reset()
        mPlayingSong.value?.let {
            mediaPlayerUtils.setDataSource(it.songUrl)
        }

    }

    fun startRandomSong() {
        songs.value?.let {
            mediaPlayerUtils.reset()
            do {
                val oldPosition = currentPlayingPosition
                currentPlayingPosition = Random.nextInt(it.size)
            } while (oldPosition == currentPlayingPosition)
            mPlayingSong.value = it[currentPlayingPosition]
            startSong()
        }
    }

    fun playMusic() {
        mIsPlaying.value = if (mediaPlayerUtils.isPlaying) {
            mediaPlayerUtils.pause()
            false
        } else {
            mediaPlayerUtils.start()
            true
        }

    }

    fun getCurrentProgress(): Int {
        if (mediaPlayerUtils.isPlaying) {
            val currentProgress =
                (mediaPlayerUtils.getCurrentPosition()
                    .toFloat() / mediaPlayerUtils.getDuration()) * 100
            return currentProgress.toInt()
        }
        return 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerUtils.getCurrentPosition()
    }

    fun getDuration(): Int {
        return mediaPlayerUtils.getDuration()
    }

    fun nextSong() {
        songs.value?.let {
            mIsPlaying.value = true
            mediaPlayerUtils.reset()
            currentPlayingPosition += 1
            if (currentPlayingPosition > it.size - 1)
                currentPlayingPosition = 0
            mPlayingSong.value = it[currentPlayingPosition]
            startSong()
        }
    }

    fun previousSong() {
        songs.value?.let {
            mIsPlaying.value = true
            mediaPlayerUtils.reset()
            currentPlayingPosition -= 1
            if (currentPlayingPosition < 0)
                currentPlayingPosition = it.size - 1
            mPlayingSong.value = it[currentPlayingPosition]
            startSong()
        }
    }

    fun setLoop(isLoop: Boolean) {
        mediaPlayerUtils.setLoop(isLoop)
    }

    fun seekTo(progress: Int) {
        val percent = progress.toFloat() / 100
        mediaPlayerUtils.seekTo((percent * getDuration()).toInt())
    }

}