package com.cojayero.dogedex2.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions,
    SignupFragment.SignUpFragmentActions {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
    }

    override fun onSignUpFieldsValidate(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        TODO("Not yet implemented")
    }
}