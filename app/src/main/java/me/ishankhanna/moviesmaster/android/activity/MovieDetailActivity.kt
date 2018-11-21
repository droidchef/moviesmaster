package me.ishankhanna.moviesmaster.android.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_movie_detail.*
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.databinding.ActivityMovieDetailBinding
import me.ishankhanna.moviesmaster.presenter.MovieDetailPresenter
import me.ishankhanna.moviesmaster.view.MovieDetailView

class MovieDetailActivity : BaseActivity<MovieDetailPresenter>(), MovieDetailView {

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.onViewCreated()

    }

    /**
     * This method is used to initialize the presenter that controls this activity a.k.a view
     */
    override fun instantiatePresenter(): MovieDetailPresenter {
        return MovieDetailPresenter(this)
    }

    /**
     * This method is used to bind the movie object to the view.
     */
    override fun showMovieDetails(movie: Movie) {
        binding.movie = movie
    }


}
