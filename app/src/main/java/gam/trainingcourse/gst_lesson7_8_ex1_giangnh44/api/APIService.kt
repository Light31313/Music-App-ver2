package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.api

import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model.Song
import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET(API.PATH)
    fun getAllMusics(): Call<List<Song>>

}