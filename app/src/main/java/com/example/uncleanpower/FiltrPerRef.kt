package com.example.uncleanpower

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.max

class FiltrPerRef {
    fun PerfectReflector(originalBitmap: Bitmap?): Bitmap? {
        val bitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
        bitmap?.apply {
            val hei = bitmap.height
            val wi = bitmap.width

            var redmax = 0
            var greenmax = 0
            var bluemax = 0

            for (i in 0 until wi) {
                for (j in 0 until hei) {

                    val color = bitmap.getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    redmax = max(redmax, r)
                    greenmax = max(greenmax, g)
                    bluemax = max(bluemax, b)
                }
            }

            for (i in 0 until wi) {
                for (j in 0 until hei) {

                    val color = bitmap.getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    val nr = r * (255 / redmax)
                    val ng = g * (255 / greenmax)
                    val nb = b * (255 / bluemax)
                    bitmap.setPixel(i, j, Color.rgb(nr, ng, nb))
                }
            }
        }
        return bitmap
    }
}