package com.zepvalue.currentburritos.model.others

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Spot(val name: String = "", val lat: Double?, val lng: Double?, val vicinity:String, val price:Int, val userRating:Int) : Parcelable