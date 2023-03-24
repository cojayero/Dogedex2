package com.cojayero.dogedex2.dogDetailActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.dogDetailActivity.ui.theme.Dogedex2Theme


private val TAG = DogDetailComposeActivity::class.java.simpleName

class DogDetailComposeActivity : ComponentActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel by viewModels<DogDetailViewModel>()

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false
        if (dog == null) {
            Toast.makeText(
                this,
                R.string.error_showing_dog_not_found,
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        setContent {
            val status = viewModel.status
            Dogedex2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DogDetailScreen(
                        finishActivity = { /*TODO*/ },
                        dog = dog,
                        isRecognition = isRecognition,
                        status = status.value
                    )
                }
            }
        }
    }
}
