package net.dev.weather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import net.dev.weather.api.WeatherServiceApi
import net.dev.weather.data.NetworkRepository
import net.dev.weather.data.WeatherRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repository: NetworkRepository): WeatherRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideWeatherService(): WeatherServiceApi {
        val logger = okhttp3.logging.HttpLoggingInterceptor().apply { level = okhttp3.logging.HttpLoggingInterceptor.Level.BASIC }

        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return retrofit2.Retrofit.Builder()
            .baseUrl(WeatherServiceApi.BASE_URL)
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(WeatherServiceApi::class.java)
    }
}