package com.zhiyouyunjing.app.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhiyouyunjing.app.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 相机Activity - 拍照识别景点
 */
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var cameraExecutor: ExecutorService

    // 权限请求
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // 检查相机权限
        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }

        setupClickListeners()
    }

    /**
     * 检查相机权限
     */
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 请求相机权限
     */
    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    /**
     * 启动相机
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // 预览
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // 图像捕获
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            try {
                // 解绑所有用例
                cameraProvider.unbindAll()

                // 绑定用例到相机
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (e: Exception) {
                Log.e(TAG, "相机绑定失败", e)
                Toast.makeText(this, "相机启动失败", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * 设置点击事件
     */
    private fun setupClickListeners() {
        // 关闭按钮
        binding.btnClose.setOnClickListener {
            finish()
        }

        // 拍照按钮
        binding.btnCapture.setOnClickListener {
            takePhoto()
        }

        // 翻转相机按钮
        binding.btnFlip.setOnClickListener {
            flipCamera()
        }

        // 相册按钮（暂时禁用）
        binding.btnGallery.setOnClickListener {
            Toast.makeText(this, "相册功能开发中", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 拍照
     */
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // 创建输出文件
        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 拍照
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "拍照成功：${photoFile.absolutePath}"
                    Toast.makeText(this@CameraActivity, "拍照成功！", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    // TODO: 跳转到识别结果页面
                    // 这里先显示成功提示，后续会添加识别功能
                    showSuccessDialog(photoFile.absolutePath)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "拍照失败: ${exc.message}", exc)
                    Toast.makeText(
                        this@CameraActivity,
                        "拍照失败",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    /**
     * 翻转相机
     */
    private fun flipCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    /**
     * 显示权限被拒绝对话框
     */
    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("需要相机权限")
            .setMessage("使用拍照识别功能需要开启相机权限")
            .setPositiveButton("去设置") { _, _ ->
                // TODO: 跳转到应用设置页面
                finish()
            }
            .setNegativeButton("取消") { _, _ ->
                finish()
            }
            .show()
    }

    /**
     * 显示拍照成功对话框
     */
    private fun showSuccessDialog(filePath: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("拍照成功")
            .setMessage("照片已保存\n\n识别功能开发中，敬请期待！")
            .setPositiveButton("确定") { _, _ ->
                finish()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}