package net.dev.weather

sealed class NavRoutes(val route: String){
    object CurrentWeather : NavRoutes("current_weather")
    object WeatherForecast : NavRoutes("weather_forecast")
}
