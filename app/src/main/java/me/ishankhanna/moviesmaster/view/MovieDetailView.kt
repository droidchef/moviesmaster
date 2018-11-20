package me.ishankhanna.moviesmaster.view

import me.ishankhanna.moviesmaster.data.model.Movie

interface MovieDetailView : MvpView {

    fun showMovieDetails(movie: Movie)

}