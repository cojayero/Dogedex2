package com.cojayero.dogedex2.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.doglist.DogRepository
import kotlinx.coroutines.launch
private val TAG = MainViewModel::class.java.simpleName
class MainViewModel:ViewModel() {
    private val _dog = MutableLiveData<Dog>()
    val dog:LiveData<Dog>
        get() =  _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>>
        get() = _status

    private val dogRepository = DogRepository()
    fun getRecognizedDog(mlDogId:String){
        Log.d(TAG, "getRecognizedDog: $mlDogId")
        viewModelScope.launch {
          handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }

    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>){
        Log.d(TAG, "handleResponseStatus: ${apiResponseStatus.javaClass}")
        if(apiResponseStatus is ApiResponseStatus.Success){
            _dog.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus
    }
}