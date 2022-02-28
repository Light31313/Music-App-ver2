package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.repository

import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.DispatcherProvider
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.api.APIService
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val apiService: APIService
) : IMusicRepository {
    override fun getAllSongs() = apiService.getAllMusics()
}