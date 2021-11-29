package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.fragment

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.adapter.ISongAdapter
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.adapter.SongAdapter
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.databinding.FragmentSongListBinding
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.entity.Song
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.service.PlayingService
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.API
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SongListFragment : Fragment(), ISongAdapter {
    private val songs = arrayListOf<Song>()
    private lateinit var rvListSong: RecyclerView
    private lateinit var binding: FragmentSongListBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var playingService: PlayingService
    private var isBound = false
    private var isSendSongs = false


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as PlayingService.PlayingBinder
            playingService = binder.getPlayingService()
            isBound = true

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
        binding = FragmentSongListBinding.inflate(inflater, container, false)
        rvListSong = binding.rvListSong
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }


    private fun initComponent() {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val adapter = activity?.let { SongAdapter(it, this, songs) }
        rvListSong.adapter = adapter
        rvListSong.layoutManager = LinearLayoutManager(context)
        getAllSongs()
    }

    // create a thread to call api to get all song data
    private fun getAllSongs() {
        API.apiService.getAllMusics().enqueue(object : Callback<MutableList<Song>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<MutableList<Song>>,
                response: Response<MutableList<Song>>
            ) {
                if (response.body() == null) {
                    return
                }
                songs.addAll(response.body()!!)
                rvListSong.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MutableList<Song>>, t: Throwable) {
                Toast.makeText(activity, "No Internet connection", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // Send all songs to PlayingService for the first time call startService
    override fun onClickItem(position: Int) {
        val intent = Intent(context, PlayingService::class.java)
        // Determine song by position in Song List
        intent.putExtra("position", position)
        if (!isSendSongs) {
            val bundle = Bundle()
            bundle.putSerializable("songs", songs)
            intent.putExtra("bundle", bundle)
            isSendSongs = true
        }
        // update UI of notification
        context?.startService(intent)

        if (!isBound) {
            context?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        // update UI of Playing Fragment
        viewModel.setSong(songs[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(context, PlayingService::class.java)
        context?.stopService(intent)

        if (isBound) {
            context?.unbindService(serviceConnection)
            isBound = false
        }
    }
}