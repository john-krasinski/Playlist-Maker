package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val MAX_HISTORY_SIZE = 10
const val HISTORY_KEY = "SEARCH_HISTORY"


class SearchHistory(val sharedPreferences: SharedPreferences,
                    val maxSize: Int = MAX_HISTORY_SIZE)
{
    private var items = mutableListOf<Track>()
    public var curSize: Int = 0
        get() {
            return items.size
        }

    init {
        load()
        sharedPreferences.registerOnSharedPreferenceChangeListener { prefs, key ->
            if (key == HISTORY_KEY) {
                load()
            }
        }
    }

    public fun add(track: Track) {
        remove(track)
        if (items.size >= MAX_HISTORY_SIZE) {
            items = items.take(9) as MutableList<Track>
        }
        items.add(0, track)
        save()
    }

    public fun remove(track: Track) {
        for (i in 0..<items.size) {
            if (items[i].trackId == track.trackId) {
                items.removeAt(i)
                break
            }
        }
    }

    public fun clear() {
        items.clear()
        save()
    }

    fun save() {
        val historyJson = Gson().toJson(items)
        sharedPreferences.edit().putString(HISTORY_KEY, historyJson).apply()
    }

    fun load() {
        val history = Gson().fromJson(sharedPreferences.getString(HISTORY_KEY, ""), Array<Track>::class.java)
        items.clear()
        if (history != null) {
            items.addAll(history.take(10))
        }
    }

    public fun get(): List<Track> {
        return items
    }
}

