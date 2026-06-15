package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dam_A51811.filmroulette.data.model.Genre


/**
 * Represents a movie entity in the local database.
 *
 * @property id The unique identifier of the movie.
 * @property title The title of the movie.
 * @property duration The duration of the movie in minutes.
 * @property synopsys A brief summary or synopsis of the movie.
 * @property imgUrl The URL of the movie's poster or cover image.
 * @property avgRating The average rating of the movie.
 * @property releaseDate The release date of the movie, if available.
 * @property originalLanguage The original language of the movie, if available.
 * @property genres The list of genres associated with the movie.
 */
@Entity(tableName = "movies")
data class Movie(
    
    @PrimaryKey
    val id: Long,

    
    val title: String,

    
    val duration: Int,

    
    val synopsys: String,

    
    @ColumnInfo(name = "img_url")
    val imgUrl: String,

    
    @ColumnInfo(name = "avg_rating")
    val avgRating: Double,

    
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,

    
    @ColumnInfo(name = "original_language")
    val originalLanguage: String?,

    
    val genres: List<Genre>
)
