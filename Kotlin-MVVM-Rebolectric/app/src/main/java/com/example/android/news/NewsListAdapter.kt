package com.example.android.news

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class NewsListAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var news = emptyList<News>()

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val _title:TextView = itemView.findViewById(R.id.text_title)
        val _image: ImageView = itemView.findViewById(R.id.image_picture)
        val _description:TextView = itemView.findViewById(R.id.text_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val current = news[position]
        holder._title.text = current.title
        Picasso.get().load(current.imageHref).error(R.drawable.ic_launcher_background).into(holder._image)
        holder._description.text=current.description
    }

    internal fun setNews(news: List<News>) {
        this.news = news
        notifyDataSetChanged()
    }

    override fun getItemCount() = news.size
}


