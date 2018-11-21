package me.ishankhanna.moviesmaster.presenter

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import me.ishankhanna.moviesmaster.data.local.MovieCache
import me.ishankhanna.moviesmaster.factory.FakeMovieDataFactory
import me.ishankhanna.moviesmaster.view.MovieDetailView
import org.junit.Before
import org.junit.Test

class MovieDetailPresenterUnitTest {

    lateinit var movieDetailPresenter: MovieDetailPresenter

    val movieDetailView = mock<MovieDetailView>()

    val movieRepository = MovieCache

    val movie = FakeMovieDataFactory.getFakeMovie()

    @Before
    fun setup() {
        movieDetailPresenter = MovieDetailPresenter(movieDetailView)
        movieRepository.selectedMovie = movie
    }

    @Test
    fun testMovieDetailsAreShown() {
        movieDetailPresenter.onViewCreated()

        verify(movieDetailView).showMovieDetails(movie)
    }

}