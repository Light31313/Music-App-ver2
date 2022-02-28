package gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.di.module

import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.DispatcherProvider
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.utils.StandardDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.api.API
import gam.trainingcourse.gst_lesson7_8_ex1_giangnh44.api.APIService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatcher()

    @Singleton
    @Provides
    fun provideAPIService(): APIService = API.apiService
}