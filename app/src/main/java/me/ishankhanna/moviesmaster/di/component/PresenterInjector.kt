package me.ishankhanna.moviesmaster.di.component

import dagger.BindsInstance
import dagger.Component
import me.ishankhanna.moviesmaster.di.module.ContextModule
import me.ishankhanna.moviesmaster.di.module.NetworkModule
import me.ishankhanna.moviesmaster.view.MvpView
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class), (ContextModule::class)])
interface PresenterInjector {

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun view(view: MvpView): Builder
    }
}