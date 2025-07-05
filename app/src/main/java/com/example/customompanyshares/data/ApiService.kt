package com.example.customompanyshares.data

import retrofit2.http.GET

interface ApiService {

    @GET("https://api.polygon.io/v2/aggs/ticker/AAPL/range/1/day/2022-01-09/2024-02-10?adjusted=true&sort=asc&limit=50000&apiKey=py8DEF3spoDthZd1cuJfTAvZ3vgEcTZU")
    suspend fun loadBars() : Result
}