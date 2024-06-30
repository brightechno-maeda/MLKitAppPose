package com.brightechno.com.maeda.mlkitapp

import android.graphics.PointF
import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(
    pose: Pose,
    imageSize: Size = Constants.IMAGE_SIZE
) {

    //val tag = "PoseGraphic"

    with(LocalDensity.current) {

        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val screenHeightDp = LocalConfiguration.current.screenHeightDp
        val scaleFactor = (screenHeightDp * density) / imageSize.height
        val offsetX =
            (screenWidthDp - screenHeightDp * imageSize.width / imageSize.height) / 2 * density

        val landmarks = pose.allPoseLandmarks
        if (landmarks.isEmpty()) {
            return
        }

        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
        val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
        val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
        val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
        val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
        val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
        val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
        val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
        val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
        val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
        val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
        val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
        val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
        val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
        val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
        val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
        val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
        val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
        val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
        val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
        val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
        val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)

        Canvas(modifier = Modifier.fillMaxSize()) {

            // Draw all the points
            val lstOft: MutableList<Offset> = mutableListOf()
            for (landmark in landmarks) {
                lstOft.add(calcOft(landmark, offsetX, scaleFactor))
            }
            drawPoints(lstOft, PointMode.Points, Color.White, 4.0f)
            // Face
            drawLine(Color.White, calcOft(nose, offsetX, scaleFactor), calcOft(leftEyeInner, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftEyeInner, offsetX, scaleFactor), calcOft(leftEye, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftEye, offsetX, scaleFactor), calcOft(leftEyeOuter, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftEyeOuter, offsetX, scaleFactor), calcOft(leftEar, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(nose, offsetX, scaleFactor), calcOft(rightEyeInner, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightEyeInner, offsetX, scaleFactor), calcOft(rightEye, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightEye, offsetX, scaleFactor), calcOft(rightEyeOuter, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightEyeOuter, offsetX, scaleFactor), calcOft(rightEar, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftMouth, offsetX, scaleFactor), calcOft(rightMouth, offsetX, scaleFactor), 3.0f)

            drawLine(Color.White, calcOft(leftShoulder, offsetX, scaleFactor), calcOft(rightShoulder, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftHip, offsetX, scaleFactor), calcOft(rightHip, offsetX, scaleFactor), 3.0f)

            // Left body
            drawLine(Color.White, calcOft(leftShoulder, offsetX, scaleFactor), calcOft(leftElbow, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftElbow, offsetX, scaleFactor), calcOft(leftWrist, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftShoulder, offsetX, scaleFactor), calcOft(leftHip, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftHip, offsetX, scaleFactor), calcOft(leftKnee, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftKnee, offsetX, scaleFactor), calcOft(leftAnkle, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftWrist, offsetX, scaleFactor), calcOft(leftThumb, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftWrist, offsetX, scaleFactor), calcOft(leftPinky, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftWrist, offsetX, scaleFactor), calcOft(leftIndex, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftIndex, offsetX, scaleFactor), calcOft(leftPinky, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftAnkle, offsetX, scaleFactor), calcOft(leftHeel, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(leftHeel, offsetX, scaleFactor), calcOft(leftFootIndex, offsetX, scaleFactor), 3.0f)

            // Right body
            drawLine(Color.White, calcOft(rightShoulder, offsetX, scaleFactor), calcOft(rightElbow, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightElbow, offsetX, scaleFactor), calcOft(rightWrist, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightShoulder, offsetX, scaleFactor), calcOft(rightHip, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightHip, offsetX, scaleFactor), calcOft(rightKnee, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightKnee, offsetX, scaleFactor), calcOft(rightAnkle, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightWrist, offsetX, scaleFactor), calcOft(rightThumb, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightWrist, offsetX, scaleFactor), calcOft(rightPinky, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightWrist, offsetX, scaleFactor), calcOft(rightIndex, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightIndex, offsetX, scaleFactor), calcOft(rightPinky, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightAnkle, offsetX, scaleFactor), calcOft(rightHeel, offsetX, scaleFactor), 3.0f)
            drawLine(Color.White, calcOft(rightHeel, offsetX, scaleFactor), calcOft(rightFootIndex, offsetX, scaleFactor), 3.0f)

        }

    }
}

// drawLine from landmark1 to landmark2
fun calcOft(
    landmark: PoseLandmark?,
    offsetX: Float,
    scaleFactor: Float,
): Offset {

    val point: PointF? = landmark?.position

    val oft = Offset(
        x = (point!!.x + offsetX) * scaleFactor,
        y = point.y * scaleFactor,
    )

    return oft

}

