package me.ishankhanna.moviesmaster.android.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.android.viewholder.MovieViewHolder
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.databinding.ItemMovieBinding

class MoviesAdapter(private val context: Context) : RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: ArrayList<Movie> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val dataBinding: ItemMovieBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun addMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }
}