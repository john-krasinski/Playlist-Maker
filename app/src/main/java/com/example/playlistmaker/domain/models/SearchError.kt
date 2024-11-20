package com.example.playlistmaker.domain.models

import android.graphics.drawable.Drawable

data class SearchError(
    val message: String,
    val image: Drawable?,
    val buttonText: String?,
    val buttonOnClick: (() -> Unit)?
)