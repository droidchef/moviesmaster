package me.ishankhanna.moviesmaster

import android.os.Bundle
import me.ishankhanna.moviesmaster.android.BaseActivity
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.presenter.MoviesListPresenter
import me.ishankhanna.moviesmaster.view.MoviesListView

class MoviesListActivity : BaseActivity<MoviesListPresenter>(), MoviesListView {
    override fun instantiatePresenter(): MoviesListPresenter {
        return MoviesListPresenter(this)
    }

    override fun showMovies(movies: List<Movie>) {
        for (movie in movies) {
            println(movie)
        }

    }

    override fun showLoading() {
        println("Loading")
    }

    override fun hideLoading() {
        println("Not Loading")
    }

    override fun showError(errorMessage: String) {
        println(errorMessage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)

        presenter.onViewCreated()

    }
}
