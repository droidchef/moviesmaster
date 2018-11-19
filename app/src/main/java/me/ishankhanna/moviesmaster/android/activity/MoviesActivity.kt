package me.ishankhanna.moviesmaster.android.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.android.adapter.MoviesAdapter
import me.ishankhanna.moviesmaster.android.divider.GridSpacingItemDecoration
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.databinding.ActivityMoviesBinding
import me.ishankhanna.moviesmaster.presenter.MoviesListPresenter
import me.ishankhanna.moviesmaster.view.MoviesListView

class MoviesActivity : BaseActivity<MoviesListPresenter>(), MoviesListView {

    private lateinit var binding: ActivityMoviesBinding

    private val moviesAdapter = MoviesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)

        binding.adapter = moviesAdapter
        binding.layoutManager = GridLayoutManager(this, 3)
        binding.dividerItemDecoration = GridSpacingItemDecoration(3, dpToPx(4), false)
        presenter.onViewCreated()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun instantiatePresenter(): MoviesListPresenter {
        return MoviesListPresenter(this)
    }

    override fun showMovies(movies: List<Movie>) {
        for (movie in movies) {
            println(movie)
        }

        moviesAdapter.addMovies(movies)

    }

    override fun showLoading() {
        binding.progressVisibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressVisibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        println(errorMessage)
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun dpToPx(dp: Int): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics))
    }

}
