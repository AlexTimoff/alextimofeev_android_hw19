package com.example.alextimofeev_android_hw18.map.data

import com.example.alextimofeev_android_hw18.map.entity.ListAttractions
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "https://overpass-api.de/api/"


object RetrofitInstance{
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val attractionsResponse: PostAllAttractionsApi = retrofit.create(PostAllAttractionsApi::class.java)
}

interface PostAllAttractionsApi {
    @POST("interpreter")
    fun postAttractions(@Body requestBody: RequestBody): Call<ListAttractions>
}