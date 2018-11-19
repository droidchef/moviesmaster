package me.ishankhanna.moviesmaster.android.binding

import android.content.res.Resources
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import me.ishankhanna.moviesmaster.android.adapter.MoviesAdapter
import me.ishankhanna.moviesmaster.android.divider.GridSpacingItemDecoration

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    val imageUrl = "https://image.tmdb.org/t/p/w500$url"
    Glide.with(view.context).load(imageUrl).into(view)
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: MoviesAdapter) {
    view.adapter = adapter
}

@BindingAdapter("layoutManager")
fun setLayoutManager(view: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
    view.layoutManager = layoutManager

    view.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(4, view.context.resources), false))
}

private fun dpToPx(dp: Int, resources: Resources): Int {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics))
}
