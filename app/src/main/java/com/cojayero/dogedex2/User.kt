package com.cojayero.dogedex2

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val id: Long,
    val email: String,
    val authenticationToken: String
) : Parcelable