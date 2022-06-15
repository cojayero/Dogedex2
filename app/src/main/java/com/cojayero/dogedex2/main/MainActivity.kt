package com.cojayero.dogedex2.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.cojayero.dogedex2.*
import com.cojayero.dogedex2.R
import com.cojayero.dogedex2.api.ApiResponseStatus
import com.cojayero.dogedex2.api.ApiServiceInterceptor
import com.cojayero.dogedex2.auth.LoginActivity
import com.cojayero.dogedex2.databinding.ActivityMainBinding
import com.cojayero.dogedex2.databinding.ActivitySettingsBinding
import com.cojayero.dogedex2.dogDetailActivity.DogDetailActivity
import com.cojayero.dogedex2.dogDetailActivity.DogDetailActivity.Companion.DOG_KEY
import com.cojayero.dogedex2.doglist.DogListActivity
import com.cojayero.dogedex2.machinelearning.Classifier
import com.cojayero.dogedex2.machinelearning.DogRecognition
import com.cojayero.dogedex2.settings.SettingsActivity
import com.cojayero.dogedex2.showPhoto.WholeImageActivity
import com.cojayero.dogedex2.showPhoto.WholeImageActivity.Companion.PHOTO_URI_KEY
import org.tensorflow.lite.support.common.FileUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var loadingWheel: ProgressBar
    private var isCameraReady: Boolean = false
    private val viewModel by viewModels<MainViewModel>()
   private lateinit var classifier:Classifier
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                //OPen Camera
                setupCamera()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Toast.makeText(
                    this,
                    "You need to accept camera permision to use camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun setLoadingWheelVisible(value: Boolean) {
        if (value) {
            loadingWheel.visibility = View.VISIBLE
        } else {
            loadingWheel.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraExecutor = Executors.newSingleThreadExecutor()
        loadingWheel = binding.loadingWheel
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
        /*
        // nos lo hemos llevado al image analyzer

        binding.takePhotoFab.setOnClickListener {
            if (isCameraReady) {
                takePhoto()
            }
        }

         */
        viewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    setLoadingWheelVisible(false)
                    Toast.makeText(this, "Error ${status.messageId}", Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> setLoadingWheelVisible(true)
                is ApiResponseStatus.Success -> setLoadingWheelVisible(false)
            }
        }
        viewModel.dog.observe(this){
            dog ->
            if(dog != null){
                openDogDetailActivity(dog)
            }

        }
        classifier = Classifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
        requestCameraPermissions()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailActivity::class.java)
        intent.putExtra(DOG_KEY,dog)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }
    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        Log.d(TAG, "openSettingsActivity: ")
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun requestCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    setupCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_request_camera_permission))
                        .setMessage(getString(R.string.request_permission_camera_rationale))
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ ->
                        }
                        .show()
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        } else {
            // OpenCamerar
            setupCamera()
        }
    }

    private fun setupCamera() {
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            startCamera()
            isCameraReady = true
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                // insert your code here.
                //...
                // after done, release the ImageProxy object
                val bitmap = convertImageProxyToBitmap(imageProxy)
                if (bitmap != null) {
                    val dogRecognition = classifier.recognizeImage(bitmap).first()
                    enabledTakePhotoButton(dogRecognition)
                }
                imageProxy.close()
            })


            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun enabledTakePhotoButton(dogRecognition: DogRecognition) {
        Log.d(TAG, "enabledTakePhotoButton: ${dogRecognition.id} , ${dogRecognition.confidence}")
        if (dogRecognition.confidence > 70.0) {
            binding.takePhotoFab.alpha = 1f
            binding.takePhotoFab.setOnClickListener {
                viewModel.getRecognizedDog(dogRecognition.id)
            }
        } else {
            binding.takePhotoFab.alpha = 0.2f
            binding.takePhotoFab.setOnClickListener(null)
        }
    }

    private fun getOutputPhotoFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            filesDir
        }
    }


    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    // insert your code here.
                    Toast.makeText(
                        this@MainActivity,
                        "Error taking foto ${error.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    /*
                    // insert your code here.
                    Log.d(TAG, "onImageSaved: ")
                    val photoUri = outputFileResults.savedUri
                    val classifier = Classifier(
                        FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
                        FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
                    )
                    val bitmap = BitmapFactory.decodeFile(photoUri?.path)
                    val dogRecognition = classifier.recognizeImage(bitmap).first().id
                    viewModel.getRecognizedDog(dogRecognition)
                     */
                }
            })
    }

    private fun openWholeImageActivity(photoUri: Uri) {
        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(PHOTO_URI_KEY, photoUri.toString())
        startActivity(intent)
    }

    fun convertImageProxyToBitmap(imageProxy: ImageProxy):Bitmap?{
        // https://stackoverflow.com/questions/56772967/converting-imageproxy-to-bitmap
        val image = imageProxy.image ?:return  null
        val yBuffer = image.planes[0].buffer // Y
        val vuBuffer = image.planes[2].buffer // VU

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}