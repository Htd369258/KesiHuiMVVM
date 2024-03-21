package com.htd.mymvvm.main.repository

import com.htd.mymvvm.base.BaseRepository
import com.htd.mymvvm.base.DataResult
import com.htd.mymvvm.utils.RxUtil
import com.htd.mymvvm.constants.EnvironmentConstants
import com.htd.mymvvm.main.response.Song
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject

class MainRepository @Inject constructor(retrofit: Retrofit) :
    BaseRepository<MainApiStore>(retrofit) {
    override fun providerRepositoryType(): Class<MainApiStore> = MainApiStore::class.java

    //rxjava用法
    fun searchSongForRxjava( term : String,
                             country : String,
                            limit : Int,
                             page : Int): Observable<DataResult<List<Song>>> {
        return apiStore.searchSongForRxjava(
            term ,
            country ,
            limit,page
        ).compose(RxUtil.transSchedule())
    }

    //协程的用法
    suspend fun searchSong(term : String,
                                      country : String,
                                      limit : Int, page : Int): DataResult<List<Song>> {
        return apiStore.searchSong(
            term ,
            country ,
            limit,page
        )
    }

}