package com.brightechno.com.maeda.mlkitapp
import android.util.Log
import android.util.Size
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.pose.Pose
import java.util.concurrent.Executors

class MainViewModel : ViewModel() {

    private var _pose = mutableStateOf<Pose?>(null)
    val pose: State<Pose?> = _pose

    val poseAnalyzeUseCase = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        //.setTargetResolution(Constants.IMAGE_SIZE)
        .setResolutionSelector(
            ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy(AspectRatio.RATIO_16_9, AspectRatioStrategy.FALLBACK_RULE_AUTO))
                .setResolutionStrategy(ResolutionStrategy(Size(540, 960), ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER))
                .build()
        )
        .build()
        .also {
            it.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                PoseAnalyzer { pose ->
                    _pose.value = pose
                    //Log.d("OnTextDetected", it.toString())
                    //Log.d("OnTextDetected", "ViewModel")
                },
            )
        }

}
