package me.ishankhanna.moviesmaster.view

import android.support.annotation.StringRes
import me.ishankhanna.moviesmaster.data.model.Movie

interface MoviesListView : MvpView {

    fun showMovies(movies: List<Movie>)

    fun showLoading()

    fun hideLoading()

    fun showError(errorMessage: String)

    fun showError(@StringRes errorResId: Int) {
        this.showError(getContext().getString(errorResId))
    }

    fun showMovieDetails()

    fun notifyItemRangeInserted(startPosition: Int, itemCount: Int)

}