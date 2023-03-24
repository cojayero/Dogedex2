package com.cojayero.dogedex2.dogDetailActivity

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.doglist.DogRepository

import kotlinx.coroutines.launch

private val TAG = DogDetailViewModel::class.java.simpleName
class DogDetailViewModel:ViewModel() {
    private val _dogList = MutableLiveData<List<Dog>>()

    //generamos la lista de Dog pero no la hacemos mutable para qu eno se pueda modificar desde fuera
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    // El modelo cuando se usa XML
    private val _status = MutableLiveData< ApiResponseStatus<Any>>()
    val statusXML: LiveData<ApiResponseStatus<Any>>
        get() = _status



    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set // solo se puede setear desde dentro de la clase

    private val dogRepository = DogRepository()

    fun addDogToUser(dogId:Long){
        Log.d(TAG, "addDogToUser: $dogId")
        viewModelScope.launch {
            // cuando estbamos en XML, haciamos con _status la modificacion del estado
             // _status.value = ApiResponseStatus.Loading()
            status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))

        }
    }
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>){
        Log.d(TAG, "handleAddDogToUserResponseStatus: ")
        // _status.value = apiResponseStatus
        status.value = apiResponseStatus
    }

}