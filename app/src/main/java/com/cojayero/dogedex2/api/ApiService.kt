package com.cojayero.dogedex2.api

import com.cojayero.dogedex2.BASE_URL
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.GET_ALL_DOGS
import com.cojayero.dogedex2.api.responses.DogListApiResponse
import com.cojayero.dogedex2.api.responses.DogListResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()


interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs():DogListApiResponse
}

object DogsApi{
    val retrofitService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}