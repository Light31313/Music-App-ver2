package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Song(
    @SerializedName(ID)
    val id: Int,
    @SerializedName(SONG_IMAGE)
    val songImage: String,
    @SerializedName(SONG_NAME)
    val songName: String,
    @SerializedName(SINGER_NAME)
    val singerName: String,
    @SerializedName(SONG_URL)
    val songUrl: String
) : Serializable {
    companion object {
        const val ID = "id"
        const val SONG_IMAGE = "songImage"
        const val SONG_NAME = "songName"
        const val SINGER_NAME = "singerName"
        const val SONG_URL = "songUrl"
    }
}
