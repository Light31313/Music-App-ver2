package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.fragment.PlaySongCollapseFragment
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.fragment.SongListFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}