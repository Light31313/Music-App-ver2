package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.databinding.FragmentPlaySongExpandBinding
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service.PlayingService
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.DispatcherProvider
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.MusicManager
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel.MusicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlaySongExpandFragment : BaseFragment<FragmentPlaySongExpandBinding>() {
    companion object {
        const val TAG = "PlaySongExpandFragment"
    }

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private val viewModel: MusicViewModel by activityViewModels()

    private lateinit var playingService: PlayingService
    private var isBound: Boolean = false

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

    private var rotateAnimator: Animator? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPlaySongExpandBinding =
        FragmentPlaySongExpandBinding.inflate(inflater, container, false)


    override fun observerLiveData() = with(binding) {

        MusicManager.playingSong.observe(viewLifecycleOwner) { playingSong ->
            song = playingSong
            currentTime = 0
            totalTime = 0
        }

        MusicManager.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                rotateAnimator?.start()
                btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
            } else {
                rotateAnimator?.cancel()
                btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
            }
        }

        MusicManager.isPrepared.observe(viewLifecycleOwner) {
            totalTime = MusicManager.getDuration()
        }

        viewModel.isExpand.observe(viewLifecycleOwner) { isExpand ->
            if (isExpand && isHidden) {
                val translateUpAnimation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.translation_up_animation)
                        .apply {
                            duration = 1500
                            interpolator = DecelerateInterpolator()
                        }
                clPlayMusic.startAnimation(translateUpAnimation)
                parentFragmentManager.beginTransaction().show(this@PlaySongExpandFragment).commit()
            } else if (!isExpand && !isHidden) {
                val translateDownAnimation =
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.translation_down_animation
                    )
                        .apply {
                            duration = 1500
                            interpolator = DecelerateInterpolator()
                        }
                clPlayMusic.startAnimation(translateDownAnimation)
                parentFragmentManager.beginTransaction().hide(this@PlaySongExpandFragment).commit()
            }

        }
    }

    override fun initView(): Unit = with(binding) {
        parentFragmentManager.beginTransaction().hide(this@PlaySongExpandFragment).commit()
        initAnimation()

        lifecycleScope.launch(dispatcherProvider.default) {
            while (true) {
                if (MusicManager.isPlaying.value == true) {
                    withContext(dispatcherProvider.main){
                        sbSongProgress.progress = MusicManager.getCurrentProgress()
                        currentTime = MusicManager.getCurrentPosition()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun initAnimation() = with(binding) {
        rotateAnimator = ObjectAnimator.ofFloat(imgPlaySong, "rotation", 360F).apply {
            duration = 10000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = null
        }
    }

    override fun initEvent() = with(binding) {
        btnCollapse.setOnClickListener {
            viewModel.setExpand(false)
        }

        btnNext.setOnClickListener {
            if (isBound)
                playingService.nextSong()
        }

        btnPrev.setOnClickListener {
            if (isBound)
                playingService.prevSong()
        }

        btnLoop.setOnClickListener {
            if (MusicManager.isLooping) {
                turnOnLoopMode(false)
            } else {
                turnOnShuffleMode(false)
                turnOnLoopMode(true)
            }
        }

        btnShuffle.setOnClickListener {
            if (MusicManager.mode == MusicManager.SHUFFLE) {
                turnOnShuffleMode(false)
            } else {
                turnOnLoopMode(false)
                turnOnShuffleMode(true)
            }
        }

        btnPlay.setOnClickListener {
            if (isBound)
                playingService.playSong()
        }

        sbSongProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicManager.seekTo(progress)
                    currentTime = MusicManager.getCurrentPosition()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        }
        )

        MusicManager.mediaPlayer.setOnBufferingUpdateListener { _, percent ->
            sbSongProgress.secondaryProgress = percent
        }
    }

    private fun turnOnLoopMode(isOn: Boolean) = with(binding) {
        if (isOn) {
            MusicManager.mode = MusicManager.LOOP
            btnLoop.setBackgroundResource(R.drawable.background_chosen_button)
            MusicManager.setLoop(true)
        } else {
            MusicManager.mode = MusicManager.NORMAL
            btnLoop.setBackgroundColor(Color.TRANSPARENT)
            MusicManager.setLoop(false)
        }
    }

    private fun turnOnShuffleMode(isOn: Boolean) = with(binding) {
        if (isOn) {
            MusicManager.mode = MusicManager.SHUFFLE
            btnShuffle.setBackgroundResource(R.drawable.background_chosen_button)
        } else {
            MusicManager.mode = MusicManager.NORMAL
            btnShuffle.setBackgroundColor(Color.TRANSPARENT)
        }
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