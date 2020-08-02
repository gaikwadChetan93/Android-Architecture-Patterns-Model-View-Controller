package com.example.topratedmoviewitharchitecturepattern.top_rated

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.topratedmoviewitharchitecturepattern.R
import com.example.topratedmoviewitharchitecturepattern.model.Movie

class MovieAdapter internal constructor(context: Context, private val movies: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater
    private lateinit var mClickListener: MovieClickedListener

    init {
        this.mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.movie_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieTitle.text = movie.title
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var movieTitle: TextView

        init {
            movieTitle = itemView.findViewById(R.id.tvMovieTitle)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mClickListener.onMovieClick(view, adapterPosition)
        }
    }

    internal fun setClickListener(itemClickListener: MovieClickedListener) {
        this.mClickListener = itemClickListener
    }

    interface MovieClickedListener {
        fun onMovieClick(view: View, position: Int)
    }
}