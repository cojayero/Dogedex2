package com.cojayero.dogedex2.api.dto

import com.squareup.moshi.Json

class AddDogToUserDTO(@field:Json(name="dog_id") val dog_id:Long)