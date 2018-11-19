package me.ishankhanna.moviesmaster.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import me.ishankhanna.moviesmaster.view.MvpView

@Module
object ContextModule {

    @Provides
    @JvmStatic
    internal fun provideContext(view: MvpView): Context {
        return view.getContext()
    }

    @Provides
    @JvmStatic
    internal fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }

}