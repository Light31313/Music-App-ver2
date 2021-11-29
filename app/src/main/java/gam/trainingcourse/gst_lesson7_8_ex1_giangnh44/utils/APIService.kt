package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils

import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity.Song
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("songInfo")
    fun getAllMusics(): Call<MutableList<Song>>

    @GET("songInfo")
    fun getByAuthor(@Query("singer") author: String): Call<MutableList<Song>>

    @GET("songInfo")
    fun getByAlbum(@Query("album") album: String): Call<MutableList<Song>>
}