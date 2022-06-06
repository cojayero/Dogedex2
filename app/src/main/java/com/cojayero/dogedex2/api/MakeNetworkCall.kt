package com.cojayero.dogedex2.api

import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): ApiResponseStatus<T> = withContext(Dispatchers.IO) {
    try {
        ApiResponseStatus.Success(call())
    } catch (e: UnknownHostException) {
        ApiResponseStatus.Error(R.string.unknown_host_exception)
    } catch (e: Exception) {
        ApiResponseStatus.Error(R.string.unknown_error)
    }
}