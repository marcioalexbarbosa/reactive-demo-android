package com.example.android.reactor

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import com.example.android.reactor.adapter.DummyAdapter
import com.example.android.reactor.model.Dummy
import com.example.android.reactor.service.ApiService
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

import okio.BufferedSource
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.dummy_list_view)

        val dummyList = arrayListOf<Dummy>()

        val adapter = DummyAdapter(this, dummyList)
        listView.adapter = adapter

        val fab: View = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            loadDummies(dummyList, adapter)
        }
    }

    private fun loadDummies(
        dummyList: ArrayList<Dummy>,
        adapter: DummyAdapter
    ) {
        dummyList.clear()
        ApiService.api().stream()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { responseBody -> getObservableFromSource(responseBody.source()) }
            .doOnNext {
                Log.i(TAG, "doOnNext $it")
            }
            .subscribe({ t ->
                Log.i(TAG, "subscribe t=$t")
                dummyList.add(Gson().fromJson(t, Dummy::class.java))
                this@MainActivity.runOnUiThread {
                    adapter.updateDataSource()
                }
            }, { e ->
                Log.i(TAG, "onError e=$e")
            }, {
                Log.i(TAG, "onFinish")
            })
    }

    private fun getObservableFromSource(source: BufferedSource): Observable<String> =
        Observable.create<String> { subscriber ->
            try {
                while (!source.exhausted()) {
                    val readUtf8Line = source.readUtf8Line()!!
                    Log.i(TAG, "source on next=$readUtf8Line")
                    subscriber.onNext(readUtf8Line)
                }
                subscriber.onComplete()
            } catch (e: IOException) {
                e.printStackTrace()
                subscriber.onError(e)
            }
        }

    companion object {
        private const val TAG = "DemoReactive"
    }
}

