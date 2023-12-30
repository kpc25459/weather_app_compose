package net.dev.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.dev.weather.MainActivityUiState.Loading
import net.dev.weather.MainActivityUiState.Success
import net.dev.weather.data.model.LatandLong
import net.dev.weather.data.model.UserData
import net.dev.weather.data.repository.SettingsRepository
import javax.inject.Inject
import kotlin.text.Typography.dagger

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    //TODO: tutaj odczytuje tylko z pamięci telefonu, więc stan Loading będzie trwał milisekundę

    val uiState: StateFlow<MainActivityUiState> = settingsRepository.userData.map {
        Log.i("MainActivityViewModel", "uiState: $it")
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    suspend fun setCurrentLocation(latandLong: LatandLong) {
        settingsRepository.setCurrentDeviceLocation(latandLong)
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
