package com.example.playlistmaker.search.ui.error

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.SearchError


class SearchErrorViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val message: TextView = item.findViewById(R.id.errorPageMsg)
    private val image: ImageView = item.findViewById(R.id.errorPageImg)
    private val button: Button = item.findViewById(R.id.errorPageBtn)

    fun bind(model: SearchError) {
        message.text = model.message
        if (model.image != null) {
            image.setImageDrawable(model.image)
        }

        if (model.buttonText != null && model.buttonOnClick != null) {
            button.text = model.buttonText
            button.isVisible = true
            button.setOnClickListener {
                model.buttonOnClick.invoke()
            }
        }
    }
}
