package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.di.module

import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.api.APIService
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.repository.IMusicRepository
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.repository.MusicRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideMusicRepository(
        dispatcherProvider: DispatcherProvider,
        apiService: APIService
    ): IMusicRepository = MusicRepository(dispatcherProvider, apiService)
}