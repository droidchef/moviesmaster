package me.ishankhanna.moviesmaster.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import me.ishankhanna.moviesmaster.data.remote.service.MoviesService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideMoviesService(retrofit: Retrofit): MoviesService {
        return retrofit.create(MoviesService::class.java)
    }

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideStethoInterceptor(): StethoInterceptor {
        return StethoInterceptor()
    }

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideBaseUrl(): String {
        return "https://api.themoviedb.org/3/"
    }

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideOkHttpClient(stethoInterceptor: StethoInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addNetworkInterceptor(stethoInterceptor).build()
    }

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideRetrofit(baseUrl: String,
                                 okHttpClient: OkHttpClient,
                                 gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }


}