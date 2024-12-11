package com.example.playlistmaker.search.ui.error

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.SearchError

class SearchErrorAdapter(
    private val error: SearchError
) : RecyclerView.Adapter<SearchErrorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchErrorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.error_page, parent, false)
        return SearchErrorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchErrorViewHolder, position: Int) {
        holder.bind(error)
    }

    override fun getItemCount(): Int {
        return 1
    }
}

