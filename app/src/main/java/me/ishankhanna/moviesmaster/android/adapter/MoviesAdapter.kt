package me.ishankhanna.moviesmaster.android.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.data.model.Movie
import me.ishankhanna.moviesmaster.databinding.ItemMovieBinding

class MoviesAdapter(private val context: Context, private val onItemClickListener: MoviesAdapter.OnItemClickListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var movies: ArrayList<Movie> = arrayListOf()

    interface OnItemClickListener {
        fun onItemClicked(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val dataBinding: ItemMovieBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], onItemClickListener)
    }

    fun addMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onItemClickListener: OnItemClickListener) {
            binding.movie = movie
            binding.root.setOnClickListener { onItemClickListener.onItemClicked(movie) }
            binding.executePendingBindings()
        }

    }

}