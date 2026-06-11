package dam_A51811.filmroulette

import android.app.Application
import dam_A51811.filmroulette.data.local.AppDatabase
import dam_A51811.filmroulette.data.remote.RetrofitClient
import dam_A51811.filmroulette.data.repository.MovieRepository
import dam_A51811.filmroulette.data.repository.MovieRepositoryImpl

class FilmRouletteApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var movieRepository: MovieRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        movieRepository = MovieRepositoryImpl(
            apiService = RetrofitClient.tmdbApiService,
            movieDao = database.movieDao()
        )
    }
}
