package com.cojayero.dogedex2.auth

import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.User
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.api.DogsApi
import com.cojayero.dogedex2.api.dto.DogDTOMapper
import com.cojayero.dogedex2.api.dto.SignUpDTO
import com.cojayero.dogedex2.api.dto.UserDTOMapper
import com.cojayero.dogedex2.api.makeNetworkCall
import java.lang.Exception

class AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetworkCall {
        val signUpDTO = SignUpDTO(email,password,passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)
        if(!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }
        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
       userDTOMapper.fromUserDTOtoDomain(userDTO)
    }
}