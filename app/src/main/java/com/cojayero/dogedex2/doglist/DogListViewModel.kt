package com.cojayero.dogedex2.doglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.api.ApiResponseStatus
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class DogListViewModel : ViewModel() {
    private val _dogList = MutableLiveData<List<Dog>>()

    //generamos la lista de Dog pero no la hacemos mutable para qu eno se pueda modificar desde fuera
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    private val _status = MutableLiveData< ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status


    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
        handleResposeStatus( dogRepository.downloadDogs())


        }
    }

    private fun handleResposeStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if(apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data
        }
        // Aqui generalizamos, porque nos da igual el tipo segun manejamos loas API,
        // en _dogList.value ya si lo tomamos con el valor correto
        _status.value = apiResponseStatus as ApiResponseStatus<Any>
    }
    fun addDogToUser(dogId:String){
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))

        }
    }
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>){
        if(apiResponseStatus is ApiResponseStatus.Success){
            downloadDogs()
        }
        _status.value = apiResponseStatus
    }
}