package com.cojayero.dogedex2.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.databinding.FragmentSignupBinding
import com.cojayero.dogedex2.isValidEmail


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    interface SignUpFragmentActions {
        fun onSignUpFieldsValidate(
            email: String,
            password: String,
            passwordConfirmation: String
        )
    }

    private lateinit var signUpFragmentActions: SignUpFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpFragmentActions = try {
            context as SignUpFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement LoginFragmentActions")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater)
        setupSignUpButton()
        return binding.root
    }

    private fun setupSignUpButton() {
        binding.signUpButton.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""

        val email = binding.emailEdit.text.toString()
        if (!isValidEmail(email)) {
            binding.emailInput.error = getString(R.string.email_is_not_valid)
            return
        }
        val password = binding.passwordEdit.text.toString()
        if (password.isEmpty()) {
            binding.passwordEdit.error = getString(R.string.password_must_not_be_empty)
        }
        val passwordConfirmation = binding.confirmPasswordEdit.text.toString()
        if (passwordConfirmation.isEmpty()) {
            binding.confirmPasswordInput.error = getString(R.string.password_must_not_be_empty)
            return
        }
        if (password != passwordConfirmation) {
            binding.passwordInput.error = getString(R.string.both_passwords_must_match)
            binding.confirmPasswordInput.error = getString(R.string.both_passwords_must_match)
            return
        }

        signUpFragmentActions.onSignUpFieldsValidate(
            email,
            password,
            passwordConfirmation
        )
    }



}