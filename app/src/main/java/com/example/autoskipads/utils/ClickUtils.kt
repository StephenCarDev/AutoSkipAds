package com.example.autoskipads.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log

fun performClickByCoordinate(accessibilityService: AccessibilityService, x: Float, y: Float) {
    Log.d("ClickUtils", "click: ($x, $y)")
    val builder = GestureDescription.Builder()
    val path = Path()
    path.moveTo(x, y)
    path.lineTo(x, y)
    builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
    val gesture = builder.build()
    accessibilityService.dispatchGesture(
        gesture,
        object : AccessibilityService.GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }

            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }
        },
        null
    )
}
