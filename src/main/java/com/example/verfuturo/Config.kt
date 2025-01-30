package com.example.verfuturo

import android.content.Context

object Config {
    fun getApiKey(context: Context): String {
        return context.getString(R.string.API_KEY)
    }
}