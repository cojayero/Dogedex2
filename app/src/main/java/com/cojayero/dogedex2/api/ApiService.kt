package com.cojayero.dogedex2.api

import com.cojayero.dogedex2.*
import com.cojayero.dogedex2.api.dto.AddDogToUserDTO
import com.cojayero.dogedex2.api.dto.LoginDTO
import com.cojayero.dogedex2.api.dto.SignUpDTO
import com.cojayero.dogedex2.api.responses.DogListApiResponse
import com.cojayero.dogedex2.api.responses.AuthApiResponse
import com.cojayero.dogedex2.api.responses.DefaultResponse
import com.cojayero.dogedex2.api.responses.DogApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
private val logging = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(ApiServiceInterceptor)
    .addInterceptor(logging)
    .build()


/*
HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
logging.setLevel(Level.BODY);

OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
httpClient.addInterceptor(logging);  // <-- this is the important line!

Retrofit retrofit = new Retrofit.Builder()
.baseUrl(API_BASE_URL)
.addConverterFactory(GsonConverterFactory.create())
.client(httpClient.build())
.build();


 */
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

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @POST(ADD_DOG_TO_USER_URL)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse
    // Trae la coleccion de un usuario
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserDogs(): DogListApiResponse

    @GET(GET_DOF_BY_ML_ID)
    suspend fun getDogByMlId(@Query("ml_id") mlId:String): DogApiResponse
}

object DogsApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}