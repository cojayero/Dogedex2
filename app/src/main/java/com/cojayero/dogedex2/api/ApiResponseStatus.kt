package com.cojayero.dogedex2.api

import com.cojayero.dogedex2.Dog

sealed class ApiResponseStatus {
    class  Success(val dogList:List<Dog>):ApiResponseStatus()
    class Loading():ApiResponseStatus()
    class Error(val messageId:Int):ApiResponseStatus()
}