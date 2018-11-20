package me.ishankhanna.moviesmaster.data.repository

import me.ishankhanna.moviesmaster.data.model.Movie
import javax.inject.Singleton

@Singleton
object MovieRepository : Repository<Movie> {

    val movies: ArrayList<Movie> = arrayListOf()

    var totalPages : Int = 0

    var selectedMovie: Movie? = null

    override fun getAll(): Collection<Movie> {
        return movies
    }

    override fun addAll(items: Collection<Movie>) {
        movies.addAll(items)
    }

    override fun clear() {
        movies.clear()
    }

    fun hasSelection(): Boolean {
        return selectedMovie != null
    }

    fun hasCachedResults() : Boolean = movies.isNotEmpty()
}