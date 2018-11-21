package me.ishankhanna.moviesmaster.presenter

import me.ishankhanna.moviesmaster.di.component.DaggerPresenterInjector
import me.ishankhanna.moviesmaster.di.component.PresenterInjector
import me.ishankhanna.moviesmaster.di.module.ContextModule
import me.ishankhanna.moviesmaster.di.module.NetworkModule
import me.ishankhanna.moviesmaster.view.MvpView

abstract class BasePresenter<out V : MvpView>(protected val view: V) {

    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .view(view)
        .contextModule(ContextModule)
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * This method may be called when the presenter view is created
     */
    open fun onViewCreated() {}

    /**
     * This method may be called when the presenter view is destroyed
     */
    open fun onViewDestroyed() {}

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is MoviesListPresenter -> injector.inject(this)
            is MovieDetailPresenter -> injector.inject(this)
        }
    }
}