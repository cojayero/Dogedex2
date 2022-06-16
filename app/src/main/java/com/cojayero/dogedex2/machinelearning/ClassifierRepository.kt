package com.cojayero.dogedex2.machinelearning

import android.graphics.*
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ClassifierRepository(private val classifier: Classifier) {

    suspend fun recognizeImage(imageProxy: ImageProxy):DogRecognition = withContext(Dispatchers.IO) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val bitmap = convertImageProxyToBitmap(imageProxy)
            if (bitmap != null) {
                    classifier.recognizeImage(bitmap).first()

            }   else {
                DogRecognition("",0f)
            }
        }



    fun convertImageProxyToBitmap(imageProxy: ImageProxy): Bitmap?{
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