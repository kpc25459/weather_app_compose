package net.dev.weather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.dev.weather.api.LocationServiceApi
import net.dev.weather.network.api.WeatherServiceApi
import net.dev.weather.data.repository.LocationRepository
import net.dev.weather.data.repository.LocationRepositoryImpl
import net.dev.weather.network.repository.NetworkWeatherRepository
import net.dev.weather.data.repository.SettingsRepository
import net.dev.weather.data.repository.SettingsRepositoryImpl
import net.dev.weather.data.repository.WeatherRepository

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindWeatherRepository(repository: NetworkWeatherRepository): WeatherRepository

    @Singleton
    @Binds
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
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

    @Singleton
    @Provides
    fun provideLocationService(): LocationServiceApi {
        val logger = okhttp3.logging.HttpLoggingInterceptor().apply { level = okhttp3.logging.HttpLoggingInterceptor.Level.BASIC }

        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return retrofit2.Retrofit.Builder()
            .baseUrl(LocationServiceApi.BASE_URL)
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(LocationServiceApi::class.java)
    }
}