package me.ishankhanna.moviesmaster.data.remote.service

import io.reactivex.Observable
import me.ishankhanna.moviesmaster.data.remote.response.ListMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("search/movie?api_key=e9bb85efff73305f0824e3af9c58c7b2")
    fun getMoviesForSearchQuery(@Query("query") query: String, @Query("page") pageIndex: Int = 1): Observable<ListMoviesResponse>

}