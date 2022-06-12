package com.cojayero.dogedex2

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val id: Long,
    val email: String,
    val authenticationToken: String
) : Parcelable {
    companion object {
        // guardamos los usuarios
        private const val AUTH_PREFS = "auth_prefs"
        private const val ID_KEY = "id"
        private const val EMAIL_KEY = "email"
        private const val AUTH_TOKEN_KEY = "auth_token"
        fun setLoggedInUser(activity: Activity, user: User) {
            activity
                .getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE).also {
                    it.edit()
                        .putLong(ID_KEY, user.id)
                        .putString(EMAIL_KEY, user.email)
                        .putString(AUTH_TOKEN_KEY, user.authenticationToken)
                        .apply()
                }
        }

        fun getLoggedInUser(activity: Activity): User? {
            val prefs =
                activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE) ?: return null
            val userId = prefs.getLong(ID_KEY, 0)
            return if (userId == 0L) {
                null
            } else {
                User(
                    prefs.getLong(ID_KEY, 0),
                    prefs.getString(EMAIL_KEY, "") ?: "",
                    prefs.getString(AUTH_TOKEN_KEY, "") ?: ""
                )
            }
        }

        fun logOut(activity: Activity) {
            activity
                .getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE).also {
                    it.edit().clear().apply()
                }
        }

    }
}