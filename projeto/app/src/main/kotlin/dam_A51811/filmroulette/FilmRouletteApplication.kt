package dam_A51811.filmroulette

import android.app.Application
import dam_A51811.filmroulette.data.local.AppDatabase
import dam_A51811.filmroulette.data.remote.RetrofitClient
import dam_A51811.filmroulette.data.remote.ai.AIAssistant
import dam_A51811.filmroulette.data.remote.ai.AIAssistantFactory
import dam_A51811.filmroulette.data.repository.FriendshipRepository
import dam_A51811.filmroulette.data.repository.FriendshipRepositoryFirebase
import dam_A51811.filmroulette.data.repository.MovieRepository
import dam_A51811.filmroulette.data.repository.MovieRepositoryImpl
import dam_A51811.filmroulette.data.repository.WatchlistRepository
import dam_A51811.filmroulette.data.repository.WatchlistRepositoryFirebase
import dam_A51811.filmroulette.data.repository.SettingsRepository
import dam_A51811.filmroulette.data.repository.AuthRepository
import dam_A51811.filmroulette.data.repository.AuthRepositoryImpl
import dam_A51811.filmroulette.data.repository.GroupSessionRepository
import dam_A51811.filmroulette.data.repository.GroupSessionRepositoryFirebase
import java.util.Properties

/**
 * Main application class for the Film Roulette app.
 * Initializes and provides global dependencies such as repositories, databases, and the AI assistant.
 */
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

    lateinit var groupSessionRepository: GroupSessionRepository
        private set

    
    lateinit var aiAssistant: AIAssistant
        private set

    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     * Initializes the Room database, Firebase settings, repositories, and the AI assistant.
     */
    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getDatabase(this)

        movieRepository = MovieRepositoryImpl(
            apiService = RetrofitClient.tmdbApiService,
            movieDao   = database.movieDao()
        )

        
        val settings = com.google.firebase.firestore.firestoreSettings {
            isPersistenceEnabled = true
        }
        com.google.firebase.firestore.FirebaseFirestore.getInstance().firestoreSettings = settings

        watchlistRepository = WatchlistRepositoryFirebase(
            movieDao = database.movieDao()
        )

        friendshipRepository = FriendshipRepositoryFirebase()

        settingsRepository = SettingsRepository(this)
        authRepository = AuthRepositoryImpl()
        groupSessionRepository = GroupSessionRepositoryFirebase()

        
        val aiProperties = Properties()
        try {
            assets.open("config.properties").use { aiProperties.load(it) }
        } catch (e: Exception) {
            android.util.Log.e("FilmRoulette", "Could not load AI config.properties: ${e.message}")
        }
        
        if (BuildConfig.NIM_API_KEY.isNotEmpty()) {
            aiProperties.setProperty("NIM_API_KEY", BuildConfig.NIM_API_KEY)
        }

        aiAssistant = AIAssistantFactory.create(aiProperties)
    }
}
