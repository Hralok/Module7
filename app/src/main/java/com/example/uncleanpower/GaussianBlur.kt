package com.example.uncleanpower

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.*

class GaussianBlur {

    fun gauss(originalBitmap: Bitmap?, keefe: Int): Bitmap {
        // получает коэффициент от 1 до 10

        val bitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
        lateinit var otv: Bitmap

        val sigma = 1.0 - (keefe.toDouble() / 10)
        val rad = 5
        val mat = kernel(sigma, rad)

        println(sigma)

        bitmap?.apply {
            val hei = height
            val wi = width

            println(height)
            println(width)

            val pixels = IntArray(hei * wi)
            getPixels(pixels, 0, wi, 0, 0, wi, hei)

            val promImg = blur(pixels, wi, hei, rad, mat)
            val n = blur(promImg, hei, wi, rad, mat)

            otv = Bitmap.createBitmap(n, wi, hei, Bitmap.Config.ARGB_8888)
        }

        return otv
    }

    private fun blur(
        orig: IntArray,
        wight: Int,
        height: Int,
        rad: Int,
        mat: DoubleArray
    ): IntArray {
        val new = IntArray(height * wight)
        for (row in 0 until height) {
            var index = row
            for (col in 0 until wight) {
                var rSum = 0.0
                var gSum = 0.0
                var bSum = 0.0

                for (i in (-rad) until (rad + 1)) {
                    var currInd = col + i
                    if ((currInd < 0) || (currInd >= wight))
                        currInd = 0
                    currInd += row * wight

                    rSum += orig[currInd].red.toDouble() * mat[i + rad]
                    gSum += orig[currInd].green.toDouble() * mat[i + rad]
                    bSum += orig[currInd].blue.toDouble() * mat[i + rad]
                }

                new[index] = Color.rgb(rSum.toInt(), gSum.toInt(), bSum.toInt())
                index += height
            }
        }

        return new
    }

    private fun kernel(sigma: Double, rad: Int): DoubleArray {
        val siz = 2 * rad + 1
        var sum = 0.0
        var index = 0

        val ker = DoubleArray(siz)

        for (i in (-rad) until (rad + 1)) {
            ker[index] = exp((-(i * i) / 2 * sigma * sigma) / sqrt(2 * PI) * sigma)
            sum += ker[index]
            index += 1
        }

        for (i in ker.indices) {
            ker[i] /= sum
        }

        return ker
    }
}