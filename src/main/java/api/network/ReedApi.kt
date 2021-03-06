package com.example.h_mal.androiddevelopertechtest_incrowdsports.data.network

import ReedResponse
import api.network.BasicInterceptor
import api.network.responses.ReedJobObject
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ReedApi {

    //get the job data from api call to Reed api data service
    @GET("search")
    suspend fun getReedData(@Query("keywords") keywords: String,
                            @Query("minimumSalary") minimumSalary: String) : Response<ReedResponse>

    @GET("search")
    suspend fun getReedData(@Query("keywords") keywords: String,
                            @Query("locationName") locationName: String,
                            @Query("minimumSalary") minimumSalary: String) : Response<ReedResponse>

    @GET("search")
    suspend fun getReedData(@Query("keywords") keywords: String,
                            @Query("locationName") locationName: String,
                            @Query("minimumSalary") minimumSalary: String,
                            @Query("resultsToSkip") resultToSkip: Int) : Response<ReedResponse>

    //instantiate api class
    companion object{
        operator fun invoke() : ReedApi{
            val okkHttpclient = OkHttpClient.Builder()
                    .addNetworkInterceptor(BasicInterceptor())
                    .build()

            //return api class ss retrofit client
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://www.reed.co.uk/api/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReedApi::class.java)
        }
    }

}

