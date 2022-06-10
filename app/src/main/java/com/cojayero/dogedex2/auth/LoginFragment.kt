package com.cojayero.dogedex2.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.databinding.FragmentLoginBinding
import com.cojayero.dogedex2.isValidEmail
import kotlin.ClassCastException

private val TAG = LoginFragment::class.java.simpleName
class LoginFragment : Fragment() {
    interface LoginFragmentActions {
        fun onRegisterButtonClick()
        fun onLoginFieldsValidate(email: String, password: String)
    }

    private lateinit var loginFragmentActions: LoginFragmentActions
    private lateinit var binding: FragmentLoginBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginFragmentActions = try {
            context as LoginFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement LoginFragmentActions")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        binding.loginRegisterButton.setOnClickListener {
            loginFragmentActions.onRegisterButtonClick()
        }
        binding.loginButton.setOnClickListener {
            validateFields()
        }
        return binding.root
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""


        val email = binding.emailEdit.text.toString()
        Log.d(TAG, "validateFields: $email ")
        if (!isValidEmail(email)) {
            binding.emailInput.error = getString(R.string.email_is_not_valid)
            return
        }
        val password = binding.passwordEdit.text.toString()
        Log.d(TAG, "validateFields: $password")
        if (password.isEmpty()) {
            binding.passwordEdit.error = getString(R.string.password_must_not_be_empty)
        }
        Log.d(TAG, "validateFields: Salto a onLoginFieldsValidate")
        loginFragmentActions.onLoginFieldsValidate(email, password)

    }

}