package com.zepvalue.currentburritos.model.google.nearbySearch

import com.google.gson.annotations.SerializedName
import com.zepvalue.currentburritos.model.google.nearbySearch.Viewport

data class Geometry(@SerializedName("viewport")
                    val viewport: Viewport?,
                    @SerializedName("location")
                    val location: Location?)