package net.dev.weather.network.model

data class AirPollution(val main: AirPollutionMain, val components: AirPollutionComponents, val dt: Int)