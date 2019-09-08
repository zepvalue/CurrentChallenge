package com.zepvalue.currentburritos.utility

fun genDescription(userRating: Int) = when(userRating){
    in 0..50 ->  "Stay away from this place for burritos"
    in 0..100 ->  "A Discrete place for burritos"
    in 100..200 -> "A good place for burritos"
    in 200..500 -> "People seem to like this burrito place"
    in 500..1000 -> "People seem to love this burrito place"
    else -> "Not rated"
}
