package com.cojayero.dogedex2.api.responses

import com.cojayero.dogedex2.api.dto.DogDTO
import com.squareup.moshi.Json

class DogApiResponse(
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,
    val data: DogResponse
)