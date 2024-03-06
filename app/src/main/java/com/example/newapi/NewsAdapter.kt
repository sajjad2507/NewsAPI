package com.example.newapi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val newsList: List<Article>, val context: MainActivity) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]
        holder.titleTextView.text = article.title
        holder.descriptionTextView.text = article.description

        Glide.with(context)
            .load(article.urlToImage)
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, MainActivity2::class.java)

            intent.putExtra("heading", article.title)
            intent.putExtra("imageUrl", article.urlToImage)
            intent.putExtra("url", article.url)
            intent.putExtra("description", article.description)

            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}
