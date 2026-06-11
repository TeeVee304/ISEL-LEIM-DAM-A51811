package dam_A51811.filmroulette

import android.app.Application
import dam_A51811.filmroulette.data.local.AppDatabase
import dam_A51811.filmroulette.data.remote.RetrofitClient
import dam_A51811.filmroulette.data.repository.FriendshipRepository
import dam_A51811.filmroulette.data.repository.FriendshipRepositoryImpl
import dam_A51811.filmroulette.data.repository.MovieRepository
import dam_A51811.filmroulette.data.repository.MovieRepositoryImpl
import dam_A51811.filmroulette.data.repository.WatchlistRepository
import dam_A51811.filmroulette.data.repository.WatchlistRepositoryImpl

class FilmRouletteApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var movieRepository: MovieRepository
        private set

    lateinit var watchlistRepository: WatchlistRepository
        private set

    lateinit var friendshipRepository: FriendshipRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)

        movieRepository = MovieRepositoryImpl(
            apiService = RetrofitClient.tmdbApiService,
            movieDao   = database.movieDao()
        )

        watchlistRepository = WatchlistRepositoryImpl(
            movieListDao = database.movieListDao()
        )

        friendshipRepository = FriendshipRepositoryImpl(
            friendshipDao = database.friendshipDao(),
            userDao       = database.userDao()
        )
    }
}
