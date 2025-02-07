package com.example.dictionary.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsApiClient {
    private const val BASE_URL = "https://newsapi.org/"
    const val API_KEY = "09ca538fc89247759db8fc9aabd8c5fc"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: NewsApiService = retrofit.create(NewsApiService::class.java)
} 