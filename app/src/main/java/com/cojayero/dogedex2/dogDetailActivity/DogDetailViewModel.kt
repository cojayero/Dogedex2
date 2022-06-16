package com.cojayero.dogedex2.dogDetailActivity

import android.util.Log
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

    private val _status = MutableLiveData< ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status


    private val dogRepository = DogRepository()

    fun addDogToUser(dogId:Long){
        Log.d(TAG, "addDogToUser: $dogId")
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))

        }
    }
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>){
        Log.d(TAG, "handleAddDogToUserResponseStatus: ")
        _status.value = apiResponseStatus
    }
}