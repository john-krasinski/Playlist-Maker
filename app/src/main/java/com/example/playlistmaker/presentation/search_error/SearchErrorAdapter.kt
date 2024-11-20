package com.example.playlistmaker.presentation.search_error

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.SearchError

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

