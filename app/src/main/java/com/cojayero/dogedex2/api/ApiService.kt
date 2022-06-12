package com.cojayero.dogedex2.api

import com.cojayero.dogedex2.*
import com.cojayero.dogedex2.api.dto.AddDogToUserDTO
import com.cojayero.dogedex2.api.dto.LoginDTO
import com.cojayero.dogedex2.api.dto.SignUpDTO
import com.cojayero.dogedex2.api.responses.DogListApiResponse
import com.cojayero.dogedex2.api.responses.AuthApiResponse
import com.cojayero.dogedex2.api.responses.DefaultResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val okHttpClient = OkHttpClient.Builder().addInterceptor(ApiServiceInterceptor).build()


private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()


interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}")
    @POST(ADD_DOG_TO_USER_URL)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse
}

object DogsApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}