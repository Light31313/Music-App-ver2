package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.repository

import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model.Song
import retrofit2.Call

interface IMusicRepository {
    fun getAllSongs(): Call<List<Song>>
}