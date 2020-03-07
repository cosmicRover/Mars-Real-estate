
package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

/** configure retrofit builder with scalars convert factory and baseurl*/
private val retrofit = Retrofit.Builder()
//        .addConverterFactory(ScalarsConverterFactory.create()) /**helps transform json to string */
        .addConverterFactory(MoshiConverterFactory.create()) /**converts to kotlin objects */
        .addCallAdapterFactory(CoroutineCallAdapterFactory()) /**adapter for co-routines*/
        .baseUrl(BASE_URL)
        .build()

interface MarsApiService{
    /** get request, using Deferred to convert this into an async await*/
    @GET("realestate")
    fun getProperties():
            Deferred<List<MarsProperty>>
}

/**expose and init a retrofit instance to rest of the app using the interface and retrofit val*/
public object MarsApi{
    val retrofitService: MarsApiService by lazy{
        retrofit.create(MarsApiService::class.java)
    }
}
