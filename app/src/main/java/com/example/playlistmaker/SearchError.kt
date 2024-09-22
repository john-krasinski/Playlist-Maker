package com.example.playlistmaker

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bumptech.glide.Glide

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
            button.visibility = View.VISIBLE
            button.setOnClickListener {
                model.buttonOnClick.invoke()
            }
        }
    }
}

data class SearchError(
    val message: String,
    val image: Drawable?,
    val buttonText: String?,
    val buttonOnClick: (() -> Unit)?
)