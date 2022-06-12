package com.cojayero.dogedex2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cojayero.dogedex2.api.ApiServiceInterceptor
import com.cojayero.dogedex2.auth.LoginActivity
import com.cojayero.dogedex2.databinding.ActivityMainBinding
import com.cojayero.dogedex2.databinding.ActivitySettingsBinding
import com.cojayero.dogedex2.doglist.DogListActivity
import com.cojayero.dogedex2.settings.SettingsActivity
private val  TAG = MainActivity::class.java.simpleName
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            return
        } else {
            Log.d(TAG, "onCreate: ApiInterceptor ${user.authenticationToken}")
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }
        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }
        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }
    }

    private fun openDogListActivity() {
        startActivity(Intent(this,DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        Log.d(TAG, "openSettingsActivity: ")
        startActivity(Intent(this,SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}