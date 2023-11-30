package com.ackerman.intermediatesubmission.data.view_ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ackerman.intermediatesubmission.MainActivity
import com.ackerman.intermediatesubmission.data.utils.ViewModelFactory
import com.ackerman.intermediatesubmission.data.utils.reduceFileImage
import com.ackerman.intermediatesubmission.data.utils.rotateBitmap
import com.ackerman.intermediatesubmission.data.utils.uriToFile
import com.ackerman.intermediatesubmission.databinding.ActivityPostStoryBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostStoryActivity : AppCompatActivity() {
    private val postStoryBinding: ActivityPostStoryBinding by lazy {
        ActivityPostStoryBinding.inflate(layoutInflater)
    }

    private val postStoryViewModel by viewModels<PostStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(postStoryBinding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        postStoryBinding.btnGallery.setOnClickListener {
            startGallery()
        }

        postStoryBinding.btnCamera.setOnClickListener {
            if (!allPermissionGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                startCameraX()
            }
        }

        postStoryBinding.btnUpload.setOnClickListener {
            uploadStory()
        }

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    @Suppress("DEPRECATION")
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            postStoryBinding.previewImageView.setImageBitmap(result)
        }
    }
    private fun uploadStory() {
        postStoryViewModel.getUser().observe(this@PostStoryActivity){
            val token = "Bearer " + it.token
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val description = "${postStoryBinding.edtDescription.text}".toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                postStoryViewModel.getUser().observe(this) {
                    postStoryViewModel.postStory(token, imageMultipart, description)
                        .observe(this@PostStoryActivity) { addStory ->
                            when (addStory) {
                                is com.ackerman.intermediatesubmission.data.utils.Result.Success -> {
                                    showLoading(false)
                                    Toast.makeText(this, "Upload Story Berhasil", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                is com.ackerman.intermediatesubmission.data.utils.Result.Loading -> {
                                    showLoading(true)
                                }
                                is com.ackerman.intermediatesubmission.data.utils.Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, "Upload Story Gagal", Toast.LENGTH_SHORT).show()
                                }
                                else -> {}
                            }
                        }
                }
            }
        }
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            postStoryBinding.progressCircular.visibility = View.VISIBLE
        } else {
            postStoryBinding.progressCircular.visibility = View.GONE
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null
    private var result: Bitmap? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@PostStoryActivity)

            getFile = myFile
            postStoryBinding.previewImageView.setImageURI(selectedImg)
        }
    }
    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (allPermissionGranted()) {
                startCameraX()
            }
        }
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 1
    }
}