package me.ishankhanna.moviesmaster.presenter

import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.data.remote.response.ListMoviesResponse
import me.ishankhanna.moviesmaster.data.remote.service.MoviesService
import me.ishankhanna.moviesmaster.data.remote.service.SearchService
import me.ishankhanna.moviesmaster.data.repository.MovieRepository
import me.ishankhanna.moviesmaster.view.MoviesListView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesListPresenter(moviesListView: MoviesListView) : BasePresenter<MoviesListView>(moviesListView) {

    @Inject
    lateinit var moviesService: MoviesService

    @Inject
    lateinit var searchService: SearchService

    @Inject
    lateinit var movieRepository: MovieRepository

    private lateinit var compositeDisposable: CompositeDisposable

    private var moviesCount: Int = 0

    var query: String? = null

    override fun onViewCreated() {

        compositeDisposable = CompositeDisposable()

        loadMovies()

    }

    private fun loadMovies() {

        view.showLoading()

        if (movieRepository.hasCachedResults()) {
            view.hideLoading()
            view.showMovies(movieRepository.movies)
        } else {
            val disposable = getDisposableForFetchingMoviesPlayingNow()
            compositeDisposable.add(disposable)
        }

    }

    private fun getDisposableForFetchingMoviesPlayingNow(pageIndex: Int = 1): Disposable {
        return moviesService
            .getMoviesPlayingNow(pageIndex)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError { it ->
                it.printStackTrace()
                view.hideLoading()
                view.showError("Unknown Error")
            }
            .subscribe { moviesListResponse ->
                view.hideLoading()
                movieRepository.addAll(moviesListResponse.movies)
                view.showMovies(moviesListResponse.movies)
                if (pageIndex == 1) {
                    movieRepository.totalPages = moviesListResponse.totalPages
                    view.notifyItemRangeInserted(0, moviesListResponse.movies.size)
                } else {
                    view.notifyItemRangeInserted(moviesCount, moviesListResponse.movies.size)
                }
                moviesCount += moviesListResponse.movies.size
            }
    }

    fun requestNextPage(page: Int) {
        if (page <= movieRepository.totalPages && query.isNullOrBlank()) {
            compositeDisposable.add(getDisposableForFetchingMoviesPlayingNow(page))
        }
    }

    fun onMovieSelected(movie: Movie?) {
        println(movie)
        movieRepository.selectedMovie = movie
        view.showMovieDetails()
    }

    fun setupAutoCompleteSearch(subject: PublishSubject<String>) {

        compositeDisposable.add(
            subject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(Function<String, ObservableSource<ListMoviesResponse>> {query->
                    this.query = query
                    return@Function searchService.getMoviesForSearchQuery(query)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                })
                .subscribe ({ listMoviesResponse: ListMoviesResponse? ->
                    println(listMoviesResponse!!.movies)
                    view.showAutoCompleteSuggestions(listMoviesResponse.movies)
                }, {
                    it.printStackTrace()
                }))
    }

    override fun onViewDestroyed() {
        compositeDisposable.dispose()
    }
}