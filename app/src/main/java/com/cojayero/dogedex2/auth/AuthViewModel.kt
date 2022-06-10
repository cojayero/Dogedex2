package com.cojayero.dogedex2.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.User
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.doglist.DogRepository
import kotlinx.coroutines.launch
private val TAG = AuthViewModel::class.java.simpleName
class AuthViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()

    //generamos la lista de Dog pero no la hacemos mutable para qu eno se pueda modificar desde fuera
    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<ApiResponseStatus<User>>()
    val status: LiveData<ApiResponseStatus<User>>
        get() = _status


    private val authRepository = AuthRepository()


    fun signUp(email: String, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
           handleResponseStatus( authRepository.signUp(email,password,passwordConfirmation))
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<User>){
        if (apiResponseStatus is ApiResponseStatus.Success){
            Log.d(TAG, "handleResponseStatus: Response status = success")
            _user.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus
    }

    fun login(email: String, password: String) {
    viewModelScope.launch {
        Log.d(TAG, "login: Launch AuthViewModel loging $email $password")
        _status.value = ApiResponseStatus.Loading()
        handleResponseStatus(authRepository.login(email,password))
    }

    }

}