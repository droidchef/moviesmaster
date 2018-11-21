package me.ishankhanna.moviesmaster.android.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import io.reactivex.subjects.PublishSubject
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.android.adapter.MoviesAdapter
import me.ishankhanna.moviesmaster.android.divider.GridSpacingItemDecoration
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.databinding.ActivityMoviesBinding
import me.ishankhanna.moviesmaster.presenter.MoviesListPresenter
import me.ishankhanna.moviesmaster.view.MoviesListView


class MoviesActivity : BaseActivity<MoviesListPresenter>(), MoviesListView, MoviesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMoviesBinding

    private val moviesAdapter = MoviesAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies)

        binding.adapter = moviesAdapter

        val gridLayoutManager = GridLayoutManager(this, 3)

        binding.layoutManager = gridLayoutManager

        binding.dividerItemDecoration = GridSpacingItemDecoration(3, dpToPx(4), false)

        binding.rvMovies.addOnScrollListener(object : InfiniteScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                presenter.requestNextPage(page)
            }
        })
        presenter.onViewCreated()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    /**
     * This method is used to initialize the presenter that controls this activity a.k.a view.
     */
    override fun instantiatePresenter(): MoviesListPresenter {
        return MoviesListPresenter(this)
    }

    /**
     * This method is used to add data to the adapter that backs the recyclerview in this activity.
     */
    override fun showMovies(movies: List<Movie>) {
        moviesAdapter.addMovies(movies)
    }

    /**
     * This method shows the progress loader.
     */
    override fun showLoading() {
        binding.progressVisibility = View.VISIBLE
    }


    /**
     * This method hides the progress loader.
     */
    override fun hideLoading() {
        binding.progressVisibility = View.GONE
    }

    /**
     * This method is used to notify the user of any errors that might occur during session of this activity.
     */
    override fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * This method notifies the adapter about the newly inserted items.
     *
     * <i>
     *     This happens while the user scrolls, since our recycler mostly shows images, it would be very nasty to call
     *  notify data set changed every single time, when we can already compute what changed in the adapter.
     * </i>
     */
    override fun notifyItemRangeInserted(startPosition: Int, itemCount: Int) {
        moviesAdapter.notifyItemRangeInserted(startPosition, itemCount)
    }

    override fun onItemClicked(movie: Movie) {
        println(movie)
        presenter.onMovieSelected(movie)
    }

    private fun dpToPx(dp: Int): Int {
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                resources.displayMetrics
            )
        )
    }

    /**
     * This method is used to start the movie details activity whenever a user taps on the movie item in the recycler.
     */
    override fun showMovieDetails() {
        startActivity(Intent(this, MovieDetailActivity::class.java))
    }

    /**
     * A simple infinite scroll listener as the name suggests, it used to track scroll and fire callbacks when we think
     * the user is going to reach the end and we should request more data.
     *
     * This can be abstracted out to a different class possible and be extended to support more layout managers.
     */
    abstract class InfiniteScrollListener(private val layoutManager: GridLayoutManager) :
        RecyclerView.OnScrollListener() {

        private var visibleThreshold = 5

        private var currentPage = 1

        private var previousTotalItemCount = 0

        private var loading = true

        private val startingPageIndex = 1

        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            val lastVisibleItemPosition: Int = (layoutManager).findLastVisibleItemPosition()

            val totalItemCount = layoutManager.itemCount


            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex
                this.previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    this.loading = true
                }
            }

            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false
                previousTotalItemCount = totalItemCount
            }

            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++
                onLoadMore(currentPage, totalItemCount, view)
                loading = true
            }
        }

        fun resetState() {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = 0
            this.loading = true
        }


        abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.movies_list_menu, menu)

        val menuItem = menu.findItem(R.id.search_movies)
        val searchView = menuItem.actionView as SearchView

        val subject = PublishSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    subject.onNext(p0!!)
                }
                return true
            }

        })

        menuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                println("on Close called")
                presenter.onSearchClosed()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

        })

        presenter.setupAutoCompleteSearch(subject)

        return true
    }

    /**
     * This method is used to show the autocomplete suggestions related to the text typed in the search.
     */
    override fun showAutoCompleteSuggestions(movies: List<Movie>) {
        moviesAdapter.clearAllMovies()
        moviesAdapter.addMovies(movies)
        moviesAdapter.notifyDataSetChanged()
    }

    override fun pruneAutoCompleteSuggestions() {
        moviesAdapter.clearAllMovies()
        moviesAdapter.notifyDataSetChanged()
    }


}
