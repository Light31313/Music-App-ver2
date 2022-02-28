package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.model.Song
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.repository.IMusicRepository
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.MusicManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val musicRepository: IMusicRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MusicViewModel"
    }

    private val _isExpand = MutableLiveData<Boolean>()
    val isExpand: LiveData<Boolean>
        get() = _isExpand

    fun setExpand(expand: Boolean) {
        _isExpand.value = expand
    }

    fun getAllSongs() {
        musicRepository.getAllSongs().enqueue(object : Callback<List<Song>> {
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                response.body()?.let {
                    MusicManager.setSongs(it)
                }


            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }


}