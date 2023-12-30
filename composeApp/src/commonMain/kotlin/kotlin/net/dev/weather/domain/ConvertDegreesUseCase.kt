package kotlin.net.dev.weather.domain

class ConvertDegreesUseCase {
    operator fun invoke(deg: Double): String {
        if (deg > 337.5) {
            return "N";
        } else if (deg > 292.5) {
            return "NW";
        } else if (deg > 247.5) {
            return "W";
        } else if (deg > 202.5) {
            return "SW";
        } else if (deg > 157.5) {
            return "S";
        } else if (deg > 122.5) {
            return "SE";
        } else if (deg > 67.5) {
            return "E";
        } else if (deg > 22.5) {
            return "NE";
        } else {
            return "";
        }
    }
}