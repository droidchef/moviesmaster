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

    /**
     * This is the instance of the movies list that is used to display movie objects in the recycler view.
     */
    private var movies: ArrayList<Movie> = arrayListOf()

    /**
     * A simple item click listener interface that must be implemented by the Activity/Fragment where the recycler view
     * that is associated with this adapter has been created. It provides callbacks for item clicks of the recycler.
     */
    interface OnItemClickListener {
        fun onItemClicked(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val dataBinding: ItemMovieBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(dataBinding)
    }

    /**
     * This method is used to return the size of our list.
     */
    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], onItemClickListener)
    }

    /**
     * This method <b>appends</b> movies to the list that is the backing data structure of our adapter.
     */
    fun addMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
    }

    /**
     * This method is used to clear the movie list that backs this adapter.
     */
    fun clearAllMovies() {
        this.movies.clear()
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onItemClickListener: OnItemClickListener) {
            binding.movie = movie
            binding.root.setOnClickListener { onItemClickListener.onItemClicked(movie) }
            binding.executePendingBindings()
        }

    }

}