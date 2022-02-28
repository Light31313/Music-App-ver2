package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.base.BaseFragment
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.databinding.FragmentPlaySongCollapseBinding
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service.PlayingService
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.DispatcherProvider
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.MusicManager
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel.MusicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlaySongCollapseFragment : BaseFragment<FragmentPlaySongCollapseBinding>() {

    private val viewModel: MusicViewModel by activityViewModels()

    private lateinit var playingService: PlayingService
    private var isBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayingService.PlayingBinder
            playingService = binder.getPlayingService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }
    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPlaySongCollapseBinding =
        FragmentPlaySongCollapseBinding.inflate(inflater, container, false)

    override fun observerLiveData() = with(binding) {
        MusicManager.playingSong.observe(viewLifecycleOwner) { playingSong ->
            song = playingSong
        }
        MusicManager.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying)
                btnCollapsePlay.setImageResource(R.drawable.ic_baseline_pause_24)
            else
                btnCollapsePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
        viewModel.isExpand.observe(viewLifecycleOwner){ isExpand ->
            if(!isExpand && isHidden){
                val translateUpAnimation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.translation_up_animation)
                        .apply {
                            duration = 1500
                            interpolator = DecelerateInterpolator()
                        }
                llPlayCollapse.startAnimation(translateUpAnimation)
                parentFragmentManager.beginTransaction().show(this@PlaySongCollapseFragment).commit()
            }
            else if (isExpand && !isHidden){
                val translateDownAnimation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.translation_down_animation)
                        .apply {
                            duration = 1500
                            interpolator = DecelerateInterpolator()
                        }
                llPlayCollapse.startAnimation(translateDownAnimation)
                parentFragmentManager.beginTransaction().hide(this@PlaySongCollapseFragment).commit()
            }

        }
    }

    override fun initView(): Unit = with(binding){
        parentFragmentManager.beginTransaction().hide(this@PlaySongCollapseFragment).commit()
        lifecycleScope.launch(dispatcherProvider.default) {
            while (true) {
                if (MusicManager.isPlaying.value == true) {
                    withContext(dispatcherProvider.main){
                        sbCollapse.progress = MusicManager.getCurrentProgress()
                    }
                }
                delay(1000)
            }
        }
    }


    override fun initEvent() = with(binding) {

        btnCollapseNext.setOnClickListener {
            playingService.nextSong()
        }

        btnCollapsePlay.setOnClickListener {
            playingService.playSong()
        }

        btnCollapsePrev.setOnClickListener {
            playingService.prevSong()
        }

        llPlayCollapse.setOnClickListener {
            viewModel.setExpand(true)
        }

        sbCollapse.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicManager.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), PlayingService::class.java).also { intent ->
            requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connection)
        isBound = false
    }


}