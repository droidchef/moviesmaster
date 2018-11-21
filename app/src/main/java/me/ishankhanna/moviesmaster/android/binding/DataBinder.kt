package me.ishankhanna.moviesmaster.android.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import me.ishankhanna.moviesmaster.R
import me.ishankhanna.moviesmaster.android.adapter.MoviesAdapter


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {

    val options = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)

    var imageUrl = ""
    when (view.id) {
        R.id.ivMovieThumb -> {
            imageUrl = "https://image.tmdb.org/t/p/w500$url"
        }
        R.id.ivMovieBackdrop -> {
            imageUrl = "https://image.tmdb.org/t/p/w500$url"
        }
    }
    Glide.with(view.context)
        .load(imageUrl)
        .apply(options)
        .into(view)
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: MoviesAdapter) {
    view.adapter = adapter
}

@BindingAdapter("layoutManager")
fun setLayoutManager(view: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
    view.layoutManager = layoutManager
}

@BindingAdapter("dividerItemDecoration")
fun setDividerItemDecoration(view: RecyclerView, dividerItemDecoration: RecyclerView.ItemDecoration) {
    view.addItemDecoration(dividerItemDecoration)
}
