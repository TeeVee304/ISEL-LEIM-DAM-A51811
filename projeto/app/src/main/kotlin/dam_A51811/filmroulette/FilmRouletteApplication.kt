package dam_A51811.filmroulette

import android.app.Application
import dam_A51811.filmroulette.data.local.AppDatabase
import dam_A51811.filmroulette.data.remote.RetrofitClient
import dam_A51811.filmroulette.data.remote.ai.AIAssistant
import dam_A51811.filmroulette.data.remote.ai.AIAssistantFactory
import dam_A51811.filmroulette.data.repository.FriendshipRepository
import dam_A51811.filmroulette.data.repository.FriendshipRepositoryImpl
import dam_A51811.filmroulette.data.repository.MovieRepository
import dam_A51811.filmroulette.data.repository.MovieRepositoryImpl
import dam_A51811.filmroulette.data.repository.WatchlistRepository
import dam_A51811.filmroulette.data.repository.WatchlistRepositoryImpl
import dam_A51811.filmroulette.data.repository.SettingsRepository
import dam_A51811.filmroulette.data.repository.AuthRepository
import dam_A51811.filmroulette.data.repository.AuthRepositoryImpl
import java.util.Properties

class FilmRouletteApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var movieRepository: MovieRepository
        private set

    lateinit var watchlistRepository: WatchlistRepository
        private set

    lateinit var friendshipRepository: FriendshipRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var authRepository: AuthRepository
        private set

    /** The AI assistant, backed by Gemini (or NIM) as configured in assets/config.properties. */
    lateinit var aiAssistant: AIAssistant
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

        settingsRepository = SettingsRepository(this)
        authRepository = AuthRepositoryImpl()

        // Load AI config from assets/config.properties
        val aiProperties = Properties()
        try {
            assets.open("config.properties").use { aiProperties.load(it) }
        } catch (e: Exception) {
            android.util.Log.e("FilmRoulette", "Could not load AI config.properties: ${e.message}")
        }
        aiAssistant = AIAssistantFactory.create(aiProperties)
    }
}
