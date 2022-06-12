package com.cojayero.dogedex2.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.cojayero.dogedex2.MainActivity
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.User
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.databinding.ActivityLoginBinding

private val TAG = LoginActivity::class.java.simpleName
class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions,
    SignupFragment.SignUpFragmentActions {
    private val viewModel by viewModels<AuthViewModel>()
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    hideLoading(true)
                    showErrorDialog(status.messageId)
                }
                is ApiResponseStatus.Loading -> {
                    hideLoading(false)
                }
                is ApiResponseStatus.Success -> {
                    hideLoading(true)
                }
            }
        }
        viewModel.user.observe(this){
            user ->
            if (user != null){
                Log.d(TAG, "onCreate: ${user.authenticationToken}")
                User.setLoggedInUser(this,user)
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
        //matamos la actividad para que en back no vuelva a la pantalla de loging.
        finish()
    }

    private fun hideLoading(hide: Boolean) {
        Log.d(TAG, "hideLoading: $hide")
        if (hide) {
            binding.loadingWheel.visibility = View.GONE
        } else {
            binding.loadingWheel.visibility = View.VISIBLE
        }
    }

    private fun showErrorDialog(messagId: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.there_was_an_error)
            .setMessage(messagId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /* */ }
            .create()
            .show()
    }

    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
    }

    override fun onLoginFieldsValidate(email: String, password: String) {
        Log.d(TAG, "onLoginFieldsValidate: email $email password $password")
        viewModel.login(email, password)
    }

    override fun onSignUpFieldsValidate(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        Log.d(TAG, "onSignUpFieldsValidate: $email, $password , $passwordConfirmation")
        viewModel.signUp(email, password, passwordConfirmation)
    }
}