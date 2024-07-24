package com.example.alextimofeev_android_hw18.camera.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.alextimofeev_android_hw18.camera.data.App
import com.example.alextimofeev_android_hw18.camera.data.PhotoDao
import com.example.alextimofeev_android_hw18.databinding.FragmentNewPhotoBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executor


class NewPhotoFragment : Fragment() {
    private var _binding: FragmentNewPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val photoDao: PhotoDao = (requireActivity().application as App).db.photoDao()
                return PhotoViewModel(photoDao) as T
            }
        }
    }

    //Определяем механизм, через который удобно получать результат запроса на разрешения.
    //если разрешение выдано, то запускаем работу камеры
    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.setPermissionState(isGranted)
            if (isGranted) {
                startCamera()
            }
        }
    private lateinit var executor: Executor
    private var imageCapture: ImageCapture? = null

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Запуск камеры
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.focus.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, executor)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executor = ContextCompat.getMainExecutor(requireContext())

        checkPermission()

        binding.makePhotoButton.setOnClickListener {
            takePhoto()
        }

        viewModel.photoUri.onEach {uri ->
            Glide
                .with(requireContext())
                .load(uri)
                .circleCrop()
                .into(binding.imagePreview)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isPermissionGranted.onEach { isPermissionGranted ->
            binding.warning.visibility = if (isPermissionGranted)
                View.GONE
            else
                View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isPermissionGranted.onEach { isPermissionGranted ->
            binding.makePhotoButton.visibility = if (isPermissionGranted)
                View.VISIBLE
            else
                View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.isPermissionGranted.onEach { isPermissionGranted ->
            binding.imagePreview.visibility = if (isPermissionGranted)
                View.VISIBLE
            else
                View.GONE
        }.launchIn(viewLifecycleOwner.lifecycleScope)


    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        viewModel.makePhoto(imageCapture, requireContext())
    }

   //Проверка наличия разрешения
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.setPermissionState(true)
            startCamera()
        } else {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }


}