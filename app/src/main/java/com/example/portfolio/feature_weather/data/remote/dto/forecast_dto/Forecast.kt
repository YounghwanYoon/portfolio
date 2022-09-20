
import com.example.portfolio.feature_weather.domain.model.forecast.Properties
import com.example.portfolio.feature_weather.data.local.entity.forecast.ForecastEntity
import com.example.portfolio.feature_weather.data.remote.dto.forecast_dto.PropertiesDto

data class ForecastDto(
    //val geometry: Geometry,
    val properties: PropertiesDto,
    //val type: String
){
    fun toForecastEntity(): ForecastEntity {
        return ForecastEntity(
            properties = this.properties.toProperties()
        )
    }

}