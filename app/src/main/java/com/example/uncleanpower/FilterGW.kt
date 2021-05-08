package com.example.uncleanpower

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap

class FilterGW {
    fun GrayWorld(originalBitmap: Bitmap?): Bitmap? {
        var bitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
        bitmap?.apply {
            val hei = bitmap.height
            val wi = bitmap.width
            var srr = 0
            var srg = 0
            var srb = 0
            var colpx = 0
            for (i in 0 until wi) {
                for (j in 0 until hei) {
                    val color = bitmap.getPixel(i, j)
                    srr += Color.red(color)
                    srg += Color.green(color)
                    srb += Color.blue(color)
                    colpx += 1
                }
            }
            srr /= colpx
            srg /= colpx
            srb /= colpx
            val avg = (srr + srg + srb) / 3

            for (i in 0 until wi) {
                for (j in 0 until hei) {
                    val color = bitmap.getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)
                    val nr = r * (avg / srr)
                    val ng = g * (avg / srg)
                    val nb = b * (avg / srb)

                    bitmap.setPixel(i, j, Color.rgb(nr, ng, nb))
                }
            }
        }
        return bitmap
    }
}
