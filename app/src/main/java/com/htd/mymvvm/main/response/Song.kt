package com.htd.mymvvm.main.response

data class Song(
    val artistName: String,
    val trackName: String,
    val trackPrice: Double,
    val rating: String,
    val trackTimeMillis: String,
    val artworkUrl60:String
){
    fun getPriceStr():String{
        return "￥${trackPrice}"
    }
}