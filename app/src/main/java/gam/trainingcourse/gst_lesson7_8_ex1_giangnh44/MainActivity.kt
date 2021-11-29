package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.fragment.PlayingFragment
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.fragment.SongListFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_song_list, SongListFragment::class.java, null)
            .setReorderingAllowed(true)
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_playing, PlayingFragment::class.java, null)
            .setReorderingAllowed(true)
            .commit()
    }


}