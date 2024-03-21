package com.htd.mymvvm.utils

import com.htd.mymvvm.base.ApiException
import com.htd.mymvvm.base.DataResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * RxJava 的工具类
 *
 *
 * Created by htd on 18-7-23.
 */

object RxUtil {
    /**
     * 线程切换处理，io线程发布，main线程订阅
     *
     * @param <T>
     * @return
    </T> */
    fun <T> transSchedule(): ObservableTransformer<T, T> {
        return ObservableTransformer { tObservable: Observable<T> ->
            tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> transScheduleNull(): ObservableTransformer<T, T>? {
        return ObservableTransformer { tObservable: Observable<T> ->
            tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 请求结果预处理，根据DataResult的情况做具体处理
     *
     * @param <T>
     * @return
    </T> */
    fun <T> transData(): ObservableTransformer<DataResult<T>, T> {
        return ObservableTransformer { dataResultObservable: Observable<DataResult<T>> ->
            return@ObservableTransformer dataResultObservable.flatMap {
                if (it.code == 0) {
                    return@flatMap Observable.just(it.results)
                }
                return@flatMap Observable.error(ApiException(it.getMsg()!!, -1))
            }
        }
    }

//    fun <T> transResponseData(): ObservableTransformer<Response<DataResult<T>>, T> {
//        return ObservableTransformer { dataResultObservable: Observable<Response<DataResult<T>>> ->
//            dataResultObservable.flatMap(label@ Function<Response<DataResult<T>>, ObservableSource<T>> { result: Response<DataResult<T>> ->
//                if (result?.body() != null && result.body()!!.code == 200) {
//                    return@label Observable.just(result.body()!!.data)
//                }
//                if (result == null)
//                    return@label Observable.error<ApiException>(
//                        ApiException(
//                            "no result",
//                            -1
//                        )
//                    )
//                if (result != null && result.body() == null) {
//                    if (result.message() == null || result.message().isEmpty()) {
//                        return@label Observable.error<ApiException>(
//                            ApiException(
//                                "Error code:" + result.code(),
//                                result.code()
//                            )
//                        )
//                    }
//                    return@label Observable.error<ApiException>(
//                        ApiException(
//                            result.message(),
//                            result.code()
//                        )
//                    )
//                }
//                Observable.error(ApiException(result.body()!!.getMsg()!!, -1))
//            })
//        }
//    }

    /**
     * 同时做网络请求的线程切换和DataResult剥离
     *
     * @param <T>
     * @return
    </T> */
//    fun <T> transResponseDataAndSchedule(): ObservableTransformer<Response<DataResult<T>>, T> {
//        return ObservableTransformer { upstream: Observable<Response<DataResult<T>>> ->
//            upstream.compose(
//                transResponseData()
//            ).compose(transSchedule())
//        }
//    }
//    open fun <T> transResponseDataAndSchedule(): ObservableTransformer<Response<DataResult<T?>?>?, T?>? {
//        return ObservableTransformer { upstream: Observable<Response<DataResult<T>?>?> ->
//            upstream.compose<Any>(
//                RxUtil.transResponseData<Any>()
//            ).compose(transSchedule<T>())
//        }
//    }

    /**
     * 同时做网络请求的线程切换和DataResult剥离
     *
     * @param <T>
     * @return
     */
    fun <T> transDataAndSchedule(): ObservableTransformer<DataResult<T>, T> {
        return ObservableTransformer {
            it.compose(transData()).compose(transSchedule())
        }
    }
}