package com.cojayero.dogedex2.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.User
import com.cojayero.dogedex2.auth.LoginActivity
import com.cojayero.dogedex2.databinding.ActivityLoginBinding
import com.cojayero.dogedex2.databinding.ActivitySettingsBinding

private val TAG = SettingsActivity::class.java.simpleName
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        User.logOut(this)
        val intent = Intent(this,LoginActivity::class.java)
        // borraremos las tareas que esten en marcha
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}