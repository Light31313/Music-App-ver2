package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Song(
    @SerializedName("imageMusic")
    val songImage: String,
    @SerializedName("songName")
    val name: String,
    @SerializedName("singer")
    val author: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("album")
    val album: String,
    @SerializedName("year")
    val year: Int
) : Serializable