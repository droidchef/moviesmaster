package me.ishankhanna.moviesmaster.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val video: Boolean,
    val title: String,
    val popularity: Double,
    val adult: Boolean,
    val overview: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("release_date") val releaseDate: String
)