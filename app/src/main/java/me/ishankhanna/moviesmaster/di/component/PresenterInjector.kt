package me.ishankhanna.moviesmaster.di.component

import dagger.BindsInstance
import dagger.Component
import me.ishankhanna.moviesmaster.di.module.ContextModule
import me.ishankhanna.moviesmaster.di.module.NetworkModule
import me.ishankhanna.moviesmaster.presenter.MovieDetailPresenter
import me.ishankhanna.moviesmaster.presenter.MoviesListPresenter
import me.ishankhanna.moviesmaster.view.MvpView
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class])
interface PresenterInjector {

    fun inject(moviesListPresenter: MoviesListPresenter)
    fun inject(movieDetailPresenter: MovieDetailPresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun view(view: MvpView): Builder
    }
}