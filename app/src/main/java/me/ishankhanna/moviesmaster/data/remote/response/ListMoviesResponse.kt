package me.ishankhanna.moviesmaster.data.remote.response

import com.google.gson.annotations.SerializedName
import me.ishankhanna.moviesmaster.data.model.Movie

data class ListMoviesResponse(
    val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val movies: List<Movie>
)