package dam_A51811.filmroulette.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dam_A51811.filmroulette.data.model.Movie
import dam_A51811.filmroulette.data.model.MovieList
import dam_A51811.filmroulette.data.model.Visibility
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

import dam_A51811.filmroulette.data.local.dao.MovieDAO
import dam_A51811.filmroulette.data.utils.MovieMapper.toDomainMovie

/**
 * Firebase Firestore implementation of the [WatchlistRepository] interface.
 * Handles operations related to movie lists and their contents in Firestore.
 *
 * @property db The [FirebaseFirestore] instance to use for database operations.
 * @property movieDao The [MovieDAO] instance to fetch local movie details.
 */
class WatchlistRepositoryFirebase(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val movieDao: MovieDAO
) : WatchlistRepository {

    /**
     * Creates a new movie list for a specified user in Firestore.
     *
     * @param userId The ID of the user creating the list.
     * @param name The name of the new list.
     * @param visibility The visibility setting of the new list.
     * @return The generated ID of the newly created list.
     */
    override suspend fun createList(userId: String, name: String, visibility: Visibility): String {
        val listRef = db.collection("users").document(userId).collection("movie_lists").document()
        val listId = listRef.id
        val movieList = MovieList(
            id = listId,
            userId = userId,
            name = name,
            createdAt = System.currentTimeMillis(),
            visibility = visibility
        )
        listRef.set(movieList).await()
        return listId
    }

    /**
     * Deletes a specific movie list from the current user's collections in Firestore.
     *
     * @param listId The ID of the list to delete.
     */
    override suspend fun deleteList(listId: String) {
        
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(uid).collection("movie_lists").document(listId).delete().await()
    }

    /**
     * Updates the visibility of a specific movie list owned by the current user.
     *
     * @param listId The ID of the list to update.
     * @param visibility The new visibility setting for the list.
     */
    override suspend fun updateVisibility(listId: String, visibility: Visibility) {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(uid).collection("movie_lists").document(listId)
            .update("visibility", visibility.name).await()
    }

    /**
     * Adds a movie to a specific movie list owned by the current user.
     * Retrieves movie details locally before saving to Firestore.
     *
     * @param listId The ID of the list to add the movie to.
     * @param movieId The ID of the movie to add.
     */
    override suspend fun addMovieToList(listId: String, movieId: Long) {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
        val movieRef = db.collection("users").document(uid).collection("movie_lists").document(listId)
            .collection("movies").document(movieId.toString())
            
        
        
        val localMovie = movieDao.getMovieById(movieId)
        if (localMovie != null) {
            movieRef.set(localMovie.toDomainMovie()).await()
        }
    }

    /**
     * Removes a movie from a specific movie list owned by the current user.
     *
     * @param listId The ID of the list from which to remove the movie.
     * @param movieId The ID of the movie to remove.
     */
    override suspend fun removeMovieFromList(listId: String, movieId: Long) {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(uid).collection("movie_lists").document(listId)
            .collection("movies").document(movieId.toString()).delete().await()
    }

    /**
     * Retrieves a flow of all movie lists owned by a specific user.
     *
     * @param userId The ID of the user whose lists to retrieve.
     * @return A [Flow] emitting a list of [MovieList] objects.
     */
    override fun getOwnLists(userId: String): Flow<List<MovieList>> = callbackFlow {
        val listener = db.collection("users").document(userId).collection("movie_lists")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(MovieList::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Retrieves a flow of public and friends-only movie lists owned by a specific user.
     *
     * @param ownerId The ID of the list owner.
     * @return A [Flow] emitting a list of [MovieList] objects.
     */
    override fun getFriendLists(ownerId: String): Flow<List<MovieList>> = callbackFlow {
        val listener = db.collection("users").document(ownerId).collection("movie_lists")
            .whereIn("visibility", listOf(Visibility.PUBLIC.name, Visibility.FRIENDS_ONLY.name))
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(MovieList::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Retrieves a flow of public movie lists owned by a specific user.
     *
     * @param ownerId The ID of the list owner.
     * @return A [Flow] emitting a list of [MovieList] objects.
     */
    override fun getPublicLists(ownerId: String): Flow<List<MovieList>> = callbackFlow {
        val listener = db.collection("users").document(ownerId).collection("movie_lists")
            .whereEqualTo("visibility", Visibility.PUBLIC.name)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(MovieList::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Retrieves a flow of all public movie lists across all users.
     *
     * @return A [Flow] emitting a list of public [MovieList] objects.
     */
    override fun getAllPublicLists(): Flow<List<MovieList>> = callbackFlow {
        val listener = db.collectionGroup("movie_lists")
            .whereEqualTo("visibility", Visibility.PUBLIC.name)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(MovieList::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Retrieves a flow of movies contained in a specific movie list.
     * Requires the user to be authenticated.
     *
     * @param listId The ID of the list to retrieve movies from.
     * @return A [Flow] emitting a list of [Movie] objects.
     */
    override fun getMoviesForList(listId: String): Flow<List<Movie>> = callbackFlow {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        
        
        
        val listener = db.collectionGroup("movies")
            
            
            
            
            
            
            
            .let { db.collection("users").document(uid).collection("movie_lists").document(listId).collection("movies") }
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Movie::class.java))
                }
            }
        awaitClose { listener.remove() }
    }
}
