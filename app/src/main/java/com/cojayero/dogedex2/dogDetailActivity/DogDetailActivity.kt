package com.cojayero.dogedex2.dogDetailActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.cojayero.dogedex2.Dog
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.databinding.ActivityDogDetailBinding

class DogDetailActivity : AppCompatActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val ID_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel by viewModels<DogDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val isRecognition = intent?.extras?.getBoolean(ID_RECOGNITION_KEY, false) ?: false
        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text =
            getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
        binding.dog = dog
        binding.dogImage.load(dog.imageURL)
        viewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this,    status.messageId, Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.loadingWheel.visibility = View.GONE
                    finish()
                }
            }
        }

        binding.closeButton.setOnClickListener {
            if (isRecognition) {
                viewModel.addDogToUser(dog.id)
            } else {
                finish()
            }


        }
    }
}