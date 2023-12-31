package ru.abyzbaev.mvp_github.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.mvp_github.R
import ru.abyzbaev.mvp_github.model.SearchResult
import ru.abyzbaev.mvp_github.view.search.SearchResultAdapter.SearchResultViewHolder

internal class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {
    private var results: List<SearchResult> = listOf()


    internal class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(searchResult: SearchResult) {
            itemView.findViewById<TextView>(R.id.repositoryName).text = searchResult.fullName
            itemView.findViewById<TextView>(R.id.repositoryName).setOnClickListener {
                Toast.makeText(itemView.context, searchResult.fullName, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, null)
        )
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    fun updateResults(results: List<SearchResult>){
        this.results = results
        notifyDataSetChanged()
    }
}