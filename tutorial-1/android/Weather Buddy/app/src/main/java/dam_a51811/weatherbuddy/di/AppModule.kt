package dam_a51811.weatherbuddy.di

import android.content.Context
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dam_a51811.weatherbuddy.data.remote.OpenWeatherApi
import dam_a51811.weatherbuddy.data.repository.WeatherRepositoryImpl
import dam_a51811.weatherbuddy.domain.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {
        return Retrofit.Builder()
            .baseUrl(OpenWeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherApiKey(@ApplicationContext context: Context): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            appInfo.metaData?.getString("WEATHER_API_KEY") ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: OpenWeatherApi,
        apiKey: String
    ): WeatherRepository {
        return WeatherRepositoryImpl(api, apiKey)
    }
}
