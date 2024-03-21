package com.htd.mymvvm.main.repository

import com.htd.mymvvm.base.DataResult
import com.htd.mymvvm.main.response.Song
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface MainApiStore {
    //rxjava用法
    @GET("search")
    fun searchSongForRxjava(
        @Query("term") term : String ,
        @Query("country") country : String ,
        @Query("limit") limit : Int,
        @Query("page") page : Int
    ) : Observable<DataResult<List<Song>>>
    //协程的用法
    @GET("search")
    suspend fun searchSong(
        @Query("term") term : String ,
        @Query("country") country : String ,
        @Query("limit") limit : Int,
        @Query("page") page : Int
    ) : DataResult<List<Song>>
    //协程的用法



}