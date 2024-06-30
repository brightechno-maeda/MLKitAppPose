package com.brightechno.com.maeda.mlkitapp

import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    vararg useCases: UseCase,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // CameraX プレビューUseCase
            val previewUseCase = androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            context.startCamera(
                lifecycleOwner = lifecycleOwner,
                previewUseCase,
                *useCases,
            )

            return@AndroidView previewView
        }
    )
}

// カメラのライフサイクルとViewのライフサイクルを紐づけ
private fun Context.startCamera(
    lifecycleOwner: LifecycleOwner,
    vararg useCases: UseCase,
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        // 背面カメラを使用するように設定
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, *useCases)
        } catch(e: Exception) {
            e.printStackTrace()
        }

    }, ContextCompat.getMainExecutor(this))
}
