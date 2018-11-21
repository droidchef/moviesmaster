package me.ishankhanna.moviesmaster.presenter

import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.data.remote.response.ListMoviesResponse
import me.ishankhanna.moviesmaster.data.remote.service.MoviesService
import me.ishankhanna.moviesmaster.data.remote.service.SearchService
import me.ishankhanna.moviesmaster.data.local.MovieCache
import me.ishankhanna.moviesmaster.view.MoviesListView
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesListPresenter(moviesListView: MoviesListView) : BasePresenter<MoviesListView>(moviesListView) {

    @Inject
    lateinit var moviesService: MoviesService

    @Inject
    lateinit var searchService: SearchService

    @Inject
    lateinit var movieCache: MovieCache

    private lateinit var compositeDisposable: CompositeDisposable

    private var moviesCount: Int = 0

    var query: String? = null

    /**
     * This method is called when the view is created so we can start updating the UI.
     */
    override fun onViewCreated() {
        compositeDisposable = CompositeDisposable()
        loadMoviesNowPlaying()
    }

    /**
     * This method initializes the view with the first set of movie results.
     */
    private fun loadMoviesNowPlaying() {

        view.showLoading()

        if (movieCache.hasCachedResults()) {
            view.hideLoading()
            view.showMovies(movieCache.movies)
        } else {
            val disposable = getDisposableForFetchingMoviesPlayingNow()
            compositeDisposable.add(disposable)
        }

    }

    /**
     * This method returns a disposable object to fetch movies using the MoviesService.
     *
     * @param pageIndex is the index of the page requested, by default it requests page 1
     */
    private fun getDisposableForFetchingMoviesPlayingNow(pageIndex: Int = 1): Disposable {
        return moviesService
            .getMoviesPlayingNow(pageIndex)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ moviesListResponse ->
                view.hideLoading()
                movieCache.addAll(moviesListResponse.movies)
                view.showMovies(moviesListResponse.movies)
                if (pageIndex == 1) {
                    movieCache.totalPages = moviesListResponse.totalPages
                    view.notifyItemRangeInserted(0, moviesListResponse.movies.size)
                } else {
                    view.notifyItemRangeInserted(moviesCount, moviesListResponse.movies.size)
                }
                moviesCount += moviesListResponse.movies.size
            }, { throwable ->
                view.hideLoading()
                when (throwable) {
                    is UnknownHostException -> view.showError(R.string.error_message_no_internet)
                    is SocketTimeoutException -> view.showError(R.string.error_message_no_internet)
                }
            })
    }

    /**
     * This method is used for paginating over the now playing movies response.
     */
    fun requestNextPage(page: Int) {
        if (page <= movieCache.totalPages && query.isNullOrBlank()) {
            compositeDisposable.add(getDisposableForFetchingMoviesPlayingNow(page))
        }
    }

    /**
     * This method is used to persist user's selection in local cache. Since we want to have a single source of truth
     * in the application for local movie cache we read and write from it throughout the lifetime of the app.
     *
     * In this case we set the selection here and then retrieve it in the MovieDetailsPresenter.
     */
    fun onMovieSelected(movie: Movie?) {
        movieCache.selectedMovie = movie
        view.showMovieDetails()
    }

    /**
     * This method hooks up the search component in the MoviesListActivity a PublishSubject.
     *
     * We use this to listen for changes in the search query and firing network requests smartly and updating the
     * results fetched in the recycler view.
     *
     * Since it was not very clear in the assignment specification if auto complete should be a new screen or the home
     * screen itself, I've just added the functionality here.
     *
     */
    fun setupAutoCompleteSearch(subject: PublishSubject<String>) {

        compositeDisposable.add(
            subject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(Function<String, ObservableSource<ListMoviesResponse>> { query ->
                    this.query = query
                    return@Function searchService.getMoviesForSearchQuery(query)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                })
                .subscribe({ listMoviesResponse: ListMoviesResponse? ->
                    if (listMoviesResponse != null && listMoviesResponse.movies.isNotEmpty()) {
                        view.showAutoCompleteSuggestions(listMoviesResponse.movies)
                    }
                }, { throwable ->
                    view.hideLoading()
                    when (throwable) {
                        is UnknownHostException -> view.showError(R.string.error_message_no_internet)
                        is SocketTimeoutException -> view.showError(R.string.error_message_no_internet)
                    }
                })
        )
    }

    override fun onViewDestroyed() {
        compositeDisposable.dispose()
    }

    fun onSearchClosed() {

        view.pruneAutoCompleteSuggestions()

        query = ""

        loadMoviesNowPlaying()
    }

}