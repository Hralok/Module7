package com.example.uncleanpower

import android.graphics.Bitmap
import kotlin.math.*

class Rotation {

    fun rotate(originalBitmap: Bitmap?, angle: Double): Bitmap? {

        var rotated: Bitmap? = null

        originalBitmap?.let {
            val diagonal = sqrt((originalBitmap.height.toDouble()*originalBitmap.height.toDouble()) +
                    (originalBitmap.width.toDouble()*originalBitmap.width.toDouble()))
            val diagAngle =  acos(originalBitmap.height.toDouble() / diagonal) * 180 / PI

            val height: Double
            val width: Double

            if (angle.rem(360) <= 90 || (angle.rem(360) <= 270 && angle.rem(360) > 180)){
                height = floor(cos(abs(diagAngle - angle.rem(90)) * PI / 180) * diagonal)
            }
            else {
                height = floor(cos(abs(90 - angle.rem(90) - diagAngle) * PI / 180) * diagonal)
            }
            if ((angle.rem(360) <= 180 && angle.rem(360) > 90) || (angle.rem(360) > 270)){
                width = floor(cos(abs(diagAngle - angle.rem(90)) * PI / 180) * diagonal)
            }
            else{
                width = floor(cos(abs(90 - angle.rem(90) - diagAngle) * PI / 180) * diagonal)
            }

            rotated = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)

            val originalCenterX = (originalBitmap.width - 1) / 2
            val originalCenterY = (originalBitmap.height - 1) / 2
            val targetCenterX = (width - 1) / 2
            val targetCenterY = (height - 1) / 2
            var radius: Double

            for (i in 0 until originalBitmap.width) {
                for (j in 0 until originalBitmap.height) {
                    val color = originalBitmap.getPixel(i, j)

                    radius = sqrt((originalCenterX - i.toDouble()) * (originalCenterX - i.toDouble()) +
                            (originalCenterY - j.toDouble()) * (originalCenterY - j.toDouble()))

                    var startAngle = acos(abs(originalCenterY - j.toDouble()) / radius) * 180 / PI
                    if (originalCenterX < i.toDouble() && originalCenterY >= j.toDouble()) {
                        startAngle += 0
                    } else if (originalCenterX <= i.toDouble() && originalCenterY < j.toDouble()) {
                        startAngle = 180 - startAngle
                    } else if (originalCenterX > i.toDouble() && originalCenterY <= j.toDouble()) {
                        startAngle += 180
                    } else {
                        startAngle = 360 - startAngle
                    }

                    val targetAngle = (startAngle + angle).rem(360)
                    val targetX: Int
                    val targetY: Int

                    if (targetAngle > 0 && targetAngle <= 180) {
                        targetY = (targetCenterY - cos(targetAngle * PI / 180) * radius).toInt()
                        targetX = (targetCenterX + abs(sin(targetAngle * PI / 180) * radius)).toInt()
                    }
                    else {
                        targetY = (targetCenterY - cos((360 - targetAngle) * PI / 180) * radius).toInt()
                        targetX = (targetCenterX - abs(sin(targetAngle * PI / 180) * radius)).toInt()
                    }

                    if (targetY >= 0 && targetY < height && targetX >= 0 && targetX < width) {
                        rotated?.setPixel(
                            targetX,
                            targetY,
                            color
                        )
                    }
                }
            }
        }
        return rotated
    }
}