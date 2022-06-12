package com.cojayero.dogedex2.doglist

import android.util.Log
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.api.ApiServiceInterceptor
import com.cojayero.dogedex2.api.DogsApi.retrofitService
import com.cojayero.dogedex2.api.dto.AddDogToUserDTO
import com.cojayero.dogedex2.api.dto.DogDTOMapper
import com.cojayero.dogedex2.api.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException
private val  TAG = DogRepository::class.java.simpleName
class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val doglistApiResponse = retrofitService.getAllDogs()
        val dogDTOList = doglistApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> =
        makeNetworkCall {
            Log.d(TAG, "addDogToUser: calling wiht $dogId")
            Log.d(TAG, "addDogToUser: apiKey = ${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}")

            val addDogToUserDTO = AddDogToUserDTO(dogId)
            val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)
            Log.d(TAG, "addDogToUser: ${defaultResponse.message}")
            Log.d(TAG, "addDogToUser: ${defaultResponse.isSuccess}")
            if (!defaultResponse.isSuccess) {
                Log.d(TAG, "addDogToUser: Not successful inclusion")
                throw Exception(defaultResponse.message)
            }
        }


    suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val doglistApiResponse = retrofitService.getUserDogs()
        val dogDTOList = doglistApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        Log.d(TAG, "getUserDogs: ")
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }
}