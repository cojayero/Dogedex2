package com.cojayero.dogedex2.showPhoto

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.cojayero.dogedex2.DataBinderMapperImpl
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.databinding.ActivityWholeImageBinding
import java.io.File

class WholeImageActivity : AppCompatActivity() {
    companion object {
        const val PHOTO_URI_KEY = "photo_uri"
    }

    private lateinit var binding: ActivityWholeImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWholeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val photoUri = intent.extras?.getString(PHOTO_URI_KEY)
        val uri = Uri.parse(photoUri)
        if (uri.path == null) {
            Toast.makeText(this, "Error showing image, no photo Uri", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        binding.wholeImage.load(File(uri.path))
    }
}