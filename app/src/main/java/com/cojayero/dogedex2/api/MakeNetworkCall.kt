package com.cojayero.dogedex2.api

import android.util.Log
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException
private val TAG = "MakeNetworkCall"
private const val UNAUTHORIZED_ERROR_CODE = 401
suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): ApiResponseStatus<T> = withContext(Dispatchers.IO) {
    try {
        Log.d(TAG, "makeNetworkCall:")
        ApiResponseStatus.Success(call())
    } catch (e: UnknownHostException) {
        ApiResponseStatus.Error(R.string.unknown_host_exception)
    }
    catch (e:HttpException){
        Log.d(TAG, "makeNetworkCall: ${e.localizedMessage}")
        val errorMessage = if( e.code() == UNAUTHORIZED_ERROR_CODE) {
            R.string.wrong_user_or_password
        } else {
            R.string.unknown_error
        }
        ApiResponseStatus.Error(errorMessage)
    }
    catch (e: Exception) {
        Log.d(TAG, "makeNetworkCall: ${e.localizedMessage}")
        val errorMessage = when(e.message){
            "sign_up_error"-> R.string.error_sign_up
            "sign_in_error"-> R.string.error_sign_in
            "user_already_exists" -> R.string.user_already_exists
            "error_adding_dog" -> R.string.error_adding_dog
            else -> R.string.unknown_error
        }
        ApiResponseStatus.Error(errorMessage)
    }
}