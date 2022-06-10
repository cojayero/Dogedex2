package com.cojayero.dogedex2

import android.util.Log
import android.util.Patterns
private val TAG = "utils"
fun isValidEmail(email: String?): Boolean {
    Log.d(TAG, "isValidEmail:  $email")
    if (email.isNullOrEmpty() ) return false
    return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

}