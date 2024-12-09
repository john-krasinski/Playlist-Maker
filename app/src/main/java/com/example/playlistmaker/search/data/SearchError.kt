package com.example.playlistmaker.search.data

import android.graphics.drawable.Drawable

data class SearchError(
    val message: String,
    val image: Drawable?,
    val buttonText: String?,
    val buttonOnClick: (() -> Unit)?
)