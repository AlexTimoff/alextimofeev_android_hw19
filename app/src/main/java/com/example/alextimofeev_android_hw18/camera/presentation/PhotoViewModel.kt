package com.example.alextimofeev_android_hw18.camera.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alextimofeev_android_hw18.camera.data.PhotoDao
import com.example.alextimofeev_android_hw18.camera.entity.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

class PhotoViewModel (private val photoDao: PhotoDao): ViewModel() {
    private var _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted get() = _isPermissionGranted.asStateFlow()

    private var _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri get() = _photoUri.asStateFlow()

    private lateinit var executor: Executor

    val photos: StateFlow<List<Photo>> = this.photoDao.getAllPhotos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = arrayListOf()
        )

    //Разрешение на использование камеры
    fun setPermissionState(isGranted: Boolean) {
        _isPermissionGranted.value = isGranted
    }

    //Сделать фото
    fun makePhoto(imageCapture: ImageCapture, context: Context){
        executor = ContextCompat.getMainExecutor(context)
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    _photoUri.value = outputFileResults.savedUri
                    addPhotoIntoDB()
                }
                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

    //Добавление фото в базу данных
    private fun addPhotoIntoDB(){
        val date = SimpleDateFormat(DATE_FORMAT, Locale.US).format(System.currentTimeMillis())
        val photo = Photo(
            uri = photoUri.value.toString(),
            datePhoto = date
        )
        viewModelScope.launch {
            photoDao.insert(photo)
        }
    }

    companion object{
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
        private const val DATE_FORMAT = "yyyy-MM-dd"
    }

}