package com.example.uncleanpower

import android.graphics.Bitmap
import kotlin.math.*

class GaussianBlur {

    fun blur(originalBitmap: Bitmap?): Bitmap? {
        val bitmap = originalBitmap?.copy( Bitmap.Config.ARGB_8888 , true)
        val mat = kernel() //получать размер размытия и передавать в функцию

        bitmap?.apply {
            val hei = height
            val wi = width
            var pixels = IntArray(hei * wi)
            getPixels(pixels, 0, wi, 0, 0, wi, hei)

            
        }
    }

    private fun kernel(size: Int): DoubleArray {
        val sigma = 1.0
        val kernel = DoubleArray(size)
        var sum = 0.0

        for (x in 0 until size) {
            val degree = - (x.toDouble().pow(2) / 2 * sigma.toDouble().pow(2))
            kernel[x] = (1 / (2 * PI * sigma.pow(2)).pow(0.5)) * E.pow(degree)
            sum += kernel[x]
        }

        for (x in 0 until size) {
            kernel[x] /= sum
        }

        return kernel
    }


}