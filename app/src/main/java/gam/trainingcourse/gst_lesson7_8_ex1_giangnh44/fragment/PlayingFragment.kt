package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.databinding.FragmentPlayingBinding
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity.Song
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service.PlayingService
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel.MainViewModel

class PlayingFragment : Fragment() {
    private lateinit var binding: FragmentPlayingBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var playingService: PlayingService
    private var isBound = false


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as PlayingService.PlayingBinder
            playingService = binder.getPlayingService()
            isBound = true
            // update UI when user click Play/Pause
            playingService.isPlaying.observe(viewLifecycleOwner, { isPlaying ->
                if (!isPlaying)
                    binding.btnCollapsePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                else
                    binding.btnCollapsePlay.setImageResource(R.drawable.ic_baseline_pause_24)

            })
            // update UI when user click Next/Previous
            playingService.clickNextOrPrev.observe(viewLifecycleOwner, {
                if (playingService.songs.size != 0)
                    initSongComponent(playingService.songs[playingService.currentPosition])
            })

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        initEvent()
    }

    //update UI when user navigates back to Fragment
    override fun onResume() {
        super.onResume()
        if (isBound)
            initSongComponent(playingService.songs[playingService.currentPosition])
    }

    private fun initComponent() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        parentFragmentManager.beginTransaction().hide(this).commit()

        val intent = Intent(context, PlayingService::class.java)
        context?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun initEvent() {
        viewModel.getSong().observe(viewLifecycleOwner, {
            if (isHidden)
                parentFragmentManager.beginTransaction().show(this).commit()

            initSongComponent(it)
        })


        binding.btnCollapseNext.setOnClickListener {
            playingService.nextSong()
        }

        binding.btnCollapsePlay.setOnClickListener {
            playingService.playSong()
        }

        binding.btnCollapsePrev.setOnClickListener {
            playingService.prevSong()
        }



        binding.sbCollapse.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                playingService.clickSeekBar()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                playingService.clickSeekBar()
            }

        })


    }
    private fun initSongComponent(song: Song) {
        binding.imgCollapse.let { Glide.with(this).load(song.songImage).into(it) }
        binding.txtAuthorCollapse.text = song.author
        binding.txtSongNameCollapse.text = song.name
    }


    override fun onDestroy() {
        super.onDestroy()
        context?.unbindService(serviceConnection)
        isBound = false
    }
}