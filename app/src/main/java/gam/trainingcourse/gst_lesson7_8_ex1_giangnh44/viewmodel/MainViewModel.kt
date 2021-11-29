package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity.Song

class MainViewModel : ViewModel() {

    private val song = MutableLiveData<Song>()

    fun setSong(song: Song) {
        this.song.value = song
    }

    fun getSong(): LiveData<Song> = song




}