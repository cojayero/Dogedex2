package com.cojayero.dogedex2.api.dto

import com.cojayero.dogedex2.Dog

class DogDTOMapper {
    fun fromDogDTOToDogDomain(dogDTO: DogDTO): Dog {
        return Dog(
            dogDTO.id,
            dogDTO.index,
            dogDTO.name,
            dogDTO.type,
            dogDTO.heightFemale,
            dogDTO.heightMale,
            dogDTO.imageURL,
            dogDTO.lifeExpectancy,
            dogDTO.temperament,
            dogDTO.weightFemale,
            dogDTO.weightMale
        )
    }

    fun fromDogDTOListToDogDomainList(dogDTOList: List<DogDTO>): List<Dog> {
        return dogDTOList.map { fromDogDTOToDogDomain(it) }
    }

    fun fromDogDomainToDogDTO(dog: Dog): DogDTO {
        return DogDTO(
            dog.id,
            dog.index,
            dog.name,
            dog.type,
            dog.heightFemale,
            dog.heightMale,
            dog.imageURL,
            dog.lifeExpectancy,
            dog.temperament,
            dog.weightFemale,
            dog.weightMale
        )
    }
    fun fromDogDomainListToDogDTOList(dogList:List<Dog>):List<DogDTO>{
        return dogList.map { fromDogDomainToDogDTO(it) }
    }
}