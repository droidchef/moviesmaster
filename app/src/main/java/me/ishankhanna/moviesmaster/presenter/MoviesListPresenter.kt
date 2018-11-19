package me.ishankhanna.moviesmaster.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.ishankhanna.moviesmaster.data.remote.service.MoviesService
import me.ishankhanna.moviesmaster.view.MoviesListView
import javax.inject.Inject

class MoviesListPresenter(moviesListView: MoviesListView) : BasePresenter<MoviesListView>(moviesListView) {

    @Inject
    lateinit var moviesService: MoviesService

    private var subscription: Disposable? = null


    override fun onViewCreated() {
        loadMovies()
    }

    private fun loadMovies() {

        view.showLoading()

        subscription = moviesService
            .getMoviesPlayingNow()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError {
                it -> it.printStackTrace()
                view.hideLoading()
                view.showError("Unknown Error")
            }
            .subscribe {
                    moviesListResponse -> view.hideLoading()
                view.showMovies(moviesListResponse.movies)
            }
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}