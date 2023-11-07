package net.dev.weather.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.dev.weather.database.dao.WeatherDao

@Module
@InstallIn(SingletonComponent::class)
class DaosModule {

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()
}