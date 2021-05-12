package com.example.uncleanpower

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import kotlin.math.*

class ColorCorrection {

    fun corr(originalBitmap: Bitmap?): Bitmap? {
        val bitmap = originalBitmap?.copy( Bitmap.Config.ARGB_8888 , true)
        bitmap?.apply {
            val hei = height
            val wi = width

            val redColors = arrayOf(255, 0)
            val greenColors = arrayOf(255, 0)
            val blueColors = arrayOf(255, 0)

            for (i in 0 until wi) {
                for (j in 0 until hei) {

                    val color = getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    redColors[0] = min(redColors[0], r)
                    redColors[1] = max(redColors[1], r)

                    greenColors[0] = min(greenColors[0], g)
                    greenColors[1] = max(greenColors[1], g)

                    blueColors[0] = min(blueColors[0], b)
                    blueColors[1] = max(blueColors[1], b)
                }
            }

            for (i in 0 until wi) {
                for (j in 0 until hei) {

                    val color = getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    val nr = (r - redColors[0]) * (255 - 0) / (redColors[1] - redColors[0])
                    val ng = (g - greenColors[0]) * (255 - 0) / (greenColors[1] - greenColors[0])
                    val nb = (b - blueColors[0]) * (255 - 0) / (blueColors[1] - blueColors[0])

                    setPixel(i, j, Color.rgb(nr, ng, nb))
                }
            }
        }

        return bitmap
    }
}