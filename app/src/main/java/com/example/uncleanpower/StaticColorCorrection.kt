package com.example.uncleanpower

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.*

class StaticColorCorrection {

    companion object {
        const val RED_COLOR = 0
        const val GREEN_COLOR = 1
        const val BLUE_COLOR = 2
    }

    fun corr(sourceBitmap: Bitmap?, targetBitmap: Bitmap?): Bitmap? {

        val source = sourceBitmap?.copy(Bitmap.Config.ARGB_8888, true)
        val target = targetBitmap?.copy(Bitmap.Config.ARGB_8888, true)

        val sInfo = getExpectationDeviation(source)
        val tInfo  = getExpectationDeviation(target)

        target?.apply {
            val hei = height
            val wi = width

            for (i in 0 until wi) {
                for (j in 0 until hei) {

                    val color = getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    val nr = sInfo[RED_COLOR][0] + (r - tInfo[RED_COLOR][0]) * (sInfo[RED_COLOR][1] / tInfo[RED_COLOR][1])
                    val ng = sInfo[GREEN_COLOR][0] + (g - tInfo[GREEN_COLOR][0]) * (sInfo[GREEN_COLOR][1] / tInfo[GREEN_COLOR][1])
                    val nb = sInfo[BLUE_COLOR][0] + (b - tInfo[BLUE_COLOR][0]) * (sInfo[BLUE_COLOR][1] / tInfo[BLUE_COLOR][1])

                    setPixel(i, j, Color.rgb(nr, ng, nb))
                }
            }
        }
        return target
    }

    private fun getExpectationDeviation(originalBitmap: Bitmap?): Array<Array<Int>> {
        val bitmap = originalBitmap?.copy( Bitmap.Config.ARGB_8888 , true)

        var expRed = 0 // мат. ожидания по цветам
        var expGreen = 0
        var expBlue = 0

        var erKv = 0 // мат. ожидания квадратов по цветам
        var egKv = 0
        var ebKv = 0

        bitmap?.apply {
            val hei = height
            val wi = width

            val p = 1 / (hei * wi)


            for (i in 0 until wi) {
                for (j in 0 until hei) {
                    val color = getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    expRed += r * p
                    expGreen += g * p
                    expBlue += b * p

                    erKv += r.toDouble().pow(2).toInt() * p
                    egKv += g.toDouble().pow(2).toInt() * p
                    ebKv += b.toDouble().pow(2).toInt() * p
                }
            }
        }

        val redDev = ((erKv - expRed.toDouble().pow(2)).pow(0.5)).toInt()
        val greenDev = ((egKv - expGreen.toDouble().pow(2)).pow(0.5)).toInt()
        val blueDev = ((ebKv - expBlue.toDouble().pow(2)).pow(0.5)).toInt()

        val a = arrayOf<Array<Int>>()
        a[RED_COLOR] = arrayOf(expRed, redDev)
        a[GREEN_COLOR] = arrayOf(expGreen, greenDev)
        a[BLUE_COLOR] = arrayOf(expBlue, blueDev)

        return a
    }
}


