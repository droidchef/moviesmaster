package me.ishankhanna.moviesmaster.data.remote.service

import io.reactivex.Single
import me.ishankhanna.moviesmaster.data.remote.response.ListMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/now_playing?api_key=e9bb85efff73305f0824e3af9c58c7b2")
    fun getMoviesPlayingNow(@Query("page") pageIndex: Int = 1): Single<ListMoviesResponse>

}