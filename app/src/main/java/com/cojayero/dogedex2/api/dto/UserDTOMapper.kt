package com.cojayero.dogedex2.api.dto

import android.util.Log
import com.cojayero.dogedex2.User
import kotlin.math.log

private val TAG = UserDTOMapper::class.java.simpleName
class UserDTOMapper {
    fun fromUserDTOtoDomain(userDTO: UserDTO):User {
        Log.d(TAG, "fromUserDTOtoDomain: ${userDTO.id}")
        Log.d(TAG, "fromUserDTOtoDomain: ${userDTO.email}")
        Log.d(TAG, "fromUserDTOtoDomain: ${userDTO.authenticationToken}")
        var _user:User
        if (userDTO.authenticationToken!= null) {
             _user = User(
                userDTO.id,
                userDTO.email,
                userDTO.authenticationToken
            )
        } else {
             _user = User(
                userDTO.id,
                userDTO.email,
                "userDTO.authenticationToken"
            )
        }

        Log.d(TAG, "fromUserDTOtoDomain: Retrornando $_user")
        return _user
    }
}