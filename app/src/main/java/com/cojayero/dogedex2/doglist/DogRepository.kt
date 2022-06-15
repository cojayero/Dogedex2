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
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.math.log

private val TAG = DogRepository::class.java.simpleName

class DogRepository {
    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(Dispatchers.IO) {
            val allDogsListResponseDeferred = async { downloadDogs() }
            val userDogListResponseDeferred = async { getUserDogs() }
            val allDogsListResponse = allDogsListResponseDeferred.await()
            val userDogListResponse = userDogListResponseDeferred.await()

            if (allDogsListResponse is ApiResponseStatus.Error) {
                Log.d(TAG, "getDogCollection: allDogsListResponse is error")
                allDogsListResponse
            } else if (userDogListResponse is ApiResponseStatus.Error) {
                Log.d(TAG, "getDogCollection: userDogListResponse is error")
                userDogListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Success && userDogListResponse is ApiResponseStatus.Success) {
                Log.d(TAG, "getDogCollection: both calls are ok")

                ApiResponseStatus.Success(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogListResponse.data
                    )
                )
            } else {
                Log.d(TAG, "getDogCollection: Unknown Error")
                ApiResponseStatus.Error(R.string.unknown_error)
            }
        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> =
        allDogList.map {
            if (userDogList.contains(it)) {
                it
            } else {
                Dog(
                    it.id, it.index,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    inCollection = false
                )
            }
        }.sorted()

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

    suspend fun getDogByMlId(mlDogId:String):ApiResponseStatus<Dog> = makeNetworkCall{
        Log.d(TAG, "getDogByMlId: DogId = $mlDogId")
        val response = retrofitService.getDogByMlId(mlDogId)
        if (!response.isSuccess){
            Log.d(TAG, "getDogByMlId: Error bucando el perro ${response.message}")
            throw Exception(response.message)
        }
        Log.d(TAG, "getDogByMlId: perroEncontrado ${response.data}")
        val dogDTOMapper = DogDTOMapper()

        val data = response.data
        dogDTOMapper.fromDogDTOToDogDomain(response.data.dog)
    }
}