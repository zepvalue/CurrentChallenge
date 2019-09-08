package com.zepvalue.currentburritos.model.google.nearbySearch

import com.google.gson.annotations.SerializedName
import com.zepvalue.currentburritos.model.google.nearbySearch.PhotosItem

data class ResultsItem(@SerializedName("reference")
                       val reference: String = "",
                       @SerializedName("types")
                       val types: List<String>?,
                       @SerializedName("scope")
                       val scope: String = "",
                       @SerializedName("icon")
                       val icon: String = "",
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("geometry")
                       val geometry: Geometry,
                       @SerializedName("vicinity")
                       val vicinity: String = "",
                       @SerializedName("id")
                       val id: String = "",
                       @SerializedName("photos")
                       val photos: List<PhotosItem>?,
                       @SerializedName("place_id")
                       val placeId: String = "",
                       @SerializedName("price_level")
                       val priceLevel: Int = 0,
                       @SerializedName("user_ratings_total")
                       val userRatings: Int = 0)
