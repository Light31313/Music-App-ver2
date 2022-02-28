package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.R
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.adapter.ISongAdapter
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.view.adapter.SongAdapter
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.databinding.FragmentSongListBinding
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.base.BaseFragment
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service.PlayingService
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.MusicManager
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel.MusicViewModel

@AndroidEntryPoint
class SongListFragment : BaseFragment<FragmentSongListBinding>(), ISongAdapter {

    companion object{
        const val COLLAPSE_FRAGMENT = "collapse"
        const val EXPAND_FRAGMENT = "expand"
    }

    private val viewModel: MusicViewModel by activityViewModels()

    private var adapter: SongAdapter? = null


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSongListBinding = FragmentSongListBinding.inflate(inflater, container, false)


    override fun observerLiveData() {
        MusicManager.songs.observe(viewLifecycleOwner) { songs ->
            adapter?.submitList(songs)
        }
    }

    override fun initView() {
        adapter = SongAdapter(this)
        binding.rvListSong.adapter = adapter

        childFragmentManager.beginTransaction()
            .replace(R.id.fcv_play_collapse, PlaySongCollapseFragment::class.java, null, COLLAPSE_FRAGMENT)
            .setReorderingAllowed(true)
            .commit()

        childFragmentManager.beginTransaction()
            .replace(R.id.fcv_play_expand, PlaySongExpandFragment::class.java, null, EXPAND_FRAGMENT)
            .setReorderingAllowed(true)
            .commit()

    }

    override fun initData() {
        viewModel.getAllSongs()
    }

    override fun onClickItem(position: Int) {
        MusicManager.songs.value?.let {
            MusicManager.setPlayingSong(it[position])
        }
        MusicManager.currentPlayingPosition = position
        val intent = Intent(requireContext(), PlayingService::class.java)
        requireContext().startService(intent)

        if(viewModel.isExpand.value == null){
            viewModel.setExpand(true)
        }
    }

}