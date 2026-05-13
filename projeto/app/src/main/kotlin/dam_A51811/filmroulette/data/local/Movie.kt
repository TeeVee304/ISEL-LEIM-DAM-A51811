package dam_A51811.filmroulette.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dam_A51811.filmroulette.data.model.Genre

/**
 * Room database entity representing a movie.
 *
 * The [id] is provided by the remote API and is never auto-generated locally.
 * The [genres] field is persisted as a comma-separated [String] via [GenreConverter].
 */
@Entity(tableName = "movies")
data class Movie(
    // SEM autoGenerate. O ID que a API fornecer será inserido manualmente aqui.
    @PrimaryKey
    val id: Long,

    /** Título da obra. */
    val title: String,

    /** Duração em minutos. */
    val duration: Int,

    /** Resumo do enredo. */
    val synopsys: String,

    /** URL do cartaz. */
    @ColumnInfo(name = "img_url")
    val imgUrl: String,

    /** Classificação média global. */
    @ColumnInfo(name = "avg_rating")
    val avgRating: Double,

    /**
     * Géneros cinematográficos da obra.
     *
     * Armazenado como uma String com valores separados por vírgula na base de dados (ver [GenreConverter]).
     * Exemplo: "ACTION,COMEDY"
     */
    val genres: List<Genre>
)
