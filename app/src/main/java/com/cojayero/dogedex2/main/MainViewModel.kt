package com.cojayero.dogedex2.main

import android.util.Log
import android.util.LogPrinter
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.doglist.DogRepository
import com.cojayero.dogedex2.machinelearning.Classifier
import com.cojayero.dogedex2.machinelearning.ClassifierRepository
import com.cojayero.dogedex2.machinelearning.DogRecognition
import kotlinx.coroutines.launch
import java.nio.MappedByteBuffer

private val TAG = MainViewModel::class.java.simpleName

class MainViewModel : ViewModel() {
    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog>
        get() = _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>>
        get() = _status

    private val _dogRecognition = MutableLiveData<DogRecognition>()
    val dogRecognition: LiveData<DogRecognition>
        get() = _dogRecognition


    private var dogRepository = DogRepository()
    private lateinit var classifierRepository: ClassifierRepository
    fun setupClassifier(
        tfliteModel: MappedByteBuffer,
        labels: List<String>
    ) {
        val classifier = Classifier(tfliteModel, labels)
        classifierRepository = ClassifierRepository(classifier)
    }

    fun recognizeImage(imageProxy: ImageProxy){
        viewModelScope.launch {
           _dogRecognition.value = classifierRepository.recognizeImage(imageProxy)
            imageProxy.close()
        }
    }

    fun getRecognizedDog(mlDogId: String) {
        Log.d(TAG, "getRecognizedDog: $mlDogId")
        viewModelScope.launch {
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }

    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>) {
        Log.d(TAG, "handleResponseStatus: ${apiResponseStatus.javaClass}")
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dog.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus
    }
}