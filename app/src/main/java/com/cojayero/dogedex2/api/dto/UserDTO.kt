package com.cojayero.dogedex2.api.dto

import com.squareup.moshi.Json

class UserDTO(
    val id:Long,
    val email:String,
    @field:Json(name = "authorization_token")  val authenticationToken:String
)