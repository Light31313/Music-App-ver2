package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service.PlayingService

class PlayingReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val action = p1?.getIntExtra("action", 0)

        val intent = Intent(p0, PlayingService::class.java)
        intent.putExtra("action", action)

        p0?.startService(intent)
    }
}