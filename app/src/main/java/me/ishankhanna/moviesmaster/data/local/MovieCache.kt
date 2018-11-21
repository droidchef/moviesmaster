package me.ishankhanna.moviesmaster.data.local

import me.ishankhanna.moviesmaster.data.model.Movie

object MovieCache {

    /**
     * Ideally this should be moved to a Local Cache and the repository should use concatenated observables from the
     * cache, database and network to get the latest data quickly as possible.
     */
    val movies: ArrayList<Movie> = arrayListOf()

    var totalPages: Int = 0

    var selectedMovie: Movie? = null

    fun addAll(items: Collection<Movie>) {
        movies.addAll(items)
    }

    fun hasCachedResults(): Boolean = movies.isNotEmpty()
}