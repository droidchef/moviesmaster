package me.ishankhanna.moviesmaster.presenter

import me.ishankhanna.moviesmaster.data.local.MovieCache
import me.ishankhanna.moviesmaster.view.MovieDetailView
import javax.inject.Inject

class MovieDetailPresenter(movieDetailView: MovieDetailView) : BasePresenter<MovieDetailView>(movieDetailView) {

    @Inject
    lateinit var movieRepository: MovieCache

    private fun showMovieDetails() {
        view.showMovieDetails(movieRepository.selectedMovie!!)
    }

    override fun onViewCreated() {
        showMovieDetails()
    }

}