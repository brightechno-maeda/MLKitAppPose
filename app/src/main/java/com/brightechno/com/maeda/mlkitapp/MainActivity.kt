package com.brightechno.com.maeda.mlkitapp
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brightechno.com.maeda.mlkitapp.ui.theme.MLKitAppTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RequestCameraPermission {
                Log.d("OnPermissionGranted", "Camera")
            }
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "topScreen") {
                composable(route = "topScreen") {
                    TopScreen(onClickButton = { navController.navigate("poseDetectScreen") })
                }
                composable(route = "poseDetectScreen") {
                    PoseDetectScreen(viewModel = viewModel, onClickButton = { navController.navigateUp() })
                }
            }
        }
    }
}

@Composable
private fun Activity.RequestCameraPermission(
    onPermissionGranted: () -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (!isGranted) {
            finish()
        } else {
            onPermissionGranted()
        }
    }
    LaunchedEffect(key1 = Unit) {
        launcher.launch(android.Manifest.permission.CAMERA)
    }
}

@Composable
fun TopScreen(onClickButton: ()->Unit = {}) {
    MLKitAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(48.dp))
                Text(text = "Top Menu")
                Button(onClick = onClickButton) {
                    Text(text = "Pose Detection")
                }
            }
        }
    }
}

@Composable
fun PoseDetectScreen(viewModel: MainViewModel, onClickButton: ()->Unit = {}) {
    MLKitAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                viewModel.poseAnalyzeUseCase,
            )
            viewModel.pose.value?.let {
                PoseOverlay(pose = it)
            }
        }
    }
}


class PoseAnalyzer(
    private val onPoseDetected: (Pose) -> Unit,
) : ImageAnalysis.Analyzer {

    // ポーズスキャンのオプション設定
    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector = PoseDetection.getClient(options)

    // analyzeはカメラで撮影中マイフレーム呼び出される
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image

        // カメラから上手く画像を取得することができているとき
        if (mediaImage != null) {

            // CameraXで取得した画像をInputImage形式に変換する
            val adjustedImage =
                InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

            // qrScannerに画像データを解析させる
            poseDetector.process(adjustedImage)
                .addOnSuccessListener { results -> // 画像解析に成功した場合
                    //Log.d(TAG, "On-device Text detection successful")
                    logExtrasForTesting(results)
                    if (results != null) {
                        onPoseDetected(results)
                    }
                }
                .addOnFailureListener { e -> // 画像解析に失敗した場合
                    Log.e(TAG, "Pose detection failed!", e)
                }
                .addOnCompleteListener {     // 画像完了時
                    image.close()
                }
        }
    }

    companion object {
        private const val TAG = "PoseDetector"
        private fun logExtrasForTesting(pose: Pose?) {

        }
    }

}

