package com.example.android.reactor.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming


interface Api {

    @GET("dummies")
    @Streaming
    fun stream(): Observable<ResponseBody>

}

