package net.dev.weather.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dev.weather.api.WeatherServiceApi
import javax.inject.Inject
import javax.inject.Singleton

interface LocationRepository {
    val location: Flow<String>
}

@Singleton
class LocationRepositoryImpl @Inject constructor(private val weatherServiceApi: WeatherServiceApi) : LocationRepository {

    override val location: Flow<String>
        get() = flow {
            val reverseLocationResponse = weatherServiceApi.getReverseLocation()
            val location = reverseLocationResponse.body()?.first()?.name ?: "Unknown"
            emit(location)
        }


    companion object {

    }
}


