package com.zepvalue.currentburritos.api

import com.zepvalue.currentburritos.model.google.nearbySearch.NearbySearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMethods {
    // Google Place API -- Nearby search
    @GET("place/nearbysearch/json")
    fun getNearbySearch(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") types: String,
        @Query("keyword") keywords: String,
        @Query("key") key: String
    ): Call<NearbySearch>
}