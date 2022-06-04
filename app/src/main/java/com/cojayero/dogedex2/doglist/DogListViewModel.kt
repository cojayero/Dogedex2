package com.cojayero.dogedex2.doglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex2.Dog
import kotlinx.coroutines.launch

class DogListViewModel:ViewModel() {
    private val _dogList = MutableLiveData<List<Dog>>()
    //generamos la lista de Dog pero no la hacemos mutable para qu eno se pueda modificar desde fuera
    val dogList : LiveData<List<Dog>>
        get() = _dogList

    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
        viewModelScope.launch{
           _dogList.value =  dogRepository.downloadDogs()
        }
    }
}