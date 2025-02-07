package com.example.dictionary.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("language") language: String = "tr",
        @Query("pageSize") pageSize: Int = 5,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val url: String,
    val publishedAt: String,
    val author: String?,
    val source: Source
)

data class Source(
    val id: String?,
    val name: String
) 