package com.cojayero.dogedex2.api.dto

import com.cojayero.dogedex2.User

class UserDTOMapper {
    fun fromUserDTOtoDomain(userDTO: UserDTO) = User(
            userDTO.id,
            userDTO.email,
            userDTO.authenticationToken
        )

}