package me.ishankhanna.moviesmaster.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.data.remote.service.MoviesService
import me.ishankhanna.moviesmaster.data.repository.MovieRepository
import me.ishankhanna.moviesmaster.view.MoviesListView
import javax.inject.Inject

class MoviesListPresenter(moviesListView: MoviesListView) : BasePresenter<MoviesListView>(moviesListView) {

    @Inject
    lateinit var moviesService: MoviesService

    @Inject
    lateinit var movieRepository: MovieRepository

    private lateinit var compositeDisposable: CompositeDisposable

    var moviesCount: Int = 0

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
                if (pageIndex == 1)  {
                    movieRepository.totalPages = moviesListResponse.totalPages
                    view.notifyItemRangeInserted(0, moviesListResponse.movies.size)
                } else {
                    view.notifyItemRangeInserted(moviesCount, moviesListResponse.movies.size)
                }
                moviesCount += moviesListResponse.movies.size
            }
    }

    fun requestNextPage(page: Int) {
        if (page <= movieRepository.totalPages) {
            compositeDisposable.add(getDisposableForFetchingMoviesPlayingNow(page))
        }
    }

    fun onMovieSelected(movie: Movie?) {
        println(movie)
        movieRepository.selectedMovie = movie
        view.showMovieDetails()
    }

    override fun onViewDestroyed() {
        compositeDisposable.dispose()
    }
}