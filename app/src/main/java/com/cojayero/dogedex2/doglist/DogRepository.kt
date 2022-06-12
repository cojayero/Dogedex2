package com.cojayero.dogedex2.doglist

import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.api.DogsApi.retrofitService
import com.cojayero.dogedex2.api.dto.AddDogToUserDTO
import com.cojayero.dogedex2.api.dto.DogDTOMapper
import com.cojayero.dogedex2.api.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException

class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val doglistApiResponse = retrofitService.getAllDogs()
        val dogDTOList = doglistApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun addDogToUser(dogId:String):ApiResponseStatus<Any> =
        makeNetworkCall {
            val addDogToUserDTO  = AddDogToUserDTO(dogId)
           val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)
            if (!defaultResponse.isSuccess){
                throw Exception(defaultResponse.message)
            }
        }
}