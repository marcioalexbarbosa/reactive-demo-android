package com.example.android.reactor.service

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    companion object {
        fun api(): Api = Retrofit.Builder()
            .baseUrl("http://192.168.1.175:8080/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)

    }
}