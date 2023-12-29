package com.example.autoskipads.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.util.Log

/**
 * 扫描文字，点击扫描到的第index个，默认第一个
 */
fun AccessibilityService.scanAndClickByText(scanText: String, index: Int = 0) = try {
    infoLog("scanText:$scanText")
    rootInActiveWindow?.findAccessibilityNodeInfosByText(scanText)
        ?.apply {
            (if (scanText == "工作台" && size > 1) get(1) else get(index))?.apply {
                infoLog(this.text.toString())
                val rect = Rect()
                this.getBoundsInScreen(rect)
                val x = rect.centerX()
                val y = rect.centerY()
                infoLog("x:$x, y: $y")
                performClickByCoordinate(x.toFloat(), y.toFloat())
            }
        }
} catch (e: Exception) {
    e.message?.let { errorLog(it) }
}

/**
 * 扫描控件id，点击扫描到的第index个，默认第一个
 */
fun AccessibilityService.scanAndClickById(viewId: String, index: Int = 0) = try {
    infoLog("scanViewId:$viewId")
    rootInActiveWindow?.findAccessibilityNodeInfosByViewId(viewId)
        ?.get(index)?.apply {
            infoLog(this.text.toString())
            val rect = Rect()
            this.getBoundsInScreen(rect)
            val x = rect.centerX()
            val y = rect.centerY()
            infoLog("x:$x, y: $y")
            performClickByCoordinate(x.toFloat(), y.toFloat())
        }
} catch (e: Exception) {
    e.message?.let { errorLog(it) }
}

/**
 * 辅助服务的模拟点击，根据屏幕x,y坐标值
 */
fun AccessibilityService.performClickByCoordinate(x: Float, y: Float) {
    Log.d("ClickUtils", "click: ($x, $y)")
    val builder = GestureDescription.Builder()
    val path = Path()
    path.moveTo(x, y)
    path.lineTo(x, y)
    builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
    val gesture = builder.build()
    this.dispatchGesture(
        gesture, object : AccessibilityService.GestureResultCallback() {
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
