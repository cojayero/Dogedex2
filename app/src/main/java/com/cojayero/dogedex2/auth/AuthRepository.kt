package com.cojayero.dogedex2.auth

import android.util.Log
import com.cojayero.dogedex2.User
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.api.DogsApi
import com.cojayero.dogedex2.api.dto.LoginDTO
import com.cojayero.dogedex2.api.dto.SignUpDTO
import com.cojayero.dogedex2.api.dto.UserDTOMapper
import com.cojayero.dogedex2.api.makeNetworkCall
import com.cojayero.dogedex2.api.responses.AuthApiResponse
import java.lang.Exception
import kotlin.math.log

private  val  TAG = AuthRepository::class.java.simpleName
class AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetworkCall {
        val signUpDTO = SignUpDTO(email,password,passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)
        Log.d(TAG, "signUp: $signUpResponse")
        if(!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }
        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
       userDTOMapper.fromUserDTOtoDomain(userDTO)
    }

    suspend fun login(
        email: String,
        password: String
    ): ApiResponseStatus<User> = makeNetworkCall {
        Log.d(TAG, "login: $email $password")
        val loginDTO = LoginDTO(email,password)
        Log.d(TAG, "login: $loginDTO")
        val loginResponse = DogsApi.retrofitService.login(loginDTO)
        Log.d(TAG, "login: ${loginResponse.javaClass.simpleName}")
        Log.d(TAG, "login: message ${loginResponse.message}")
        Log.d(TAG, "login: isSuccess ${loginResponse.isSuccess}")
        Log.d(TAG, "login: data ${loginResponse.data}")
        if(!loginResponse.isSuccess) {
            
            throw Exception(loginResponse.message)
        }
        val userDTO = loginResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOtoDomain(userDTO)
    }
}