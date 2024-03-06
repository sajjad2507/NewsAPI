package com.example.newapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    fun getNews(
        @Query("apiKey") apiKey: String,
        @Query("q") searchQuery: String,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "en"
    ): Call<NewsResponse>
}
