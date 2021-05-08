package com.example.uncleanpower

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap


class Filter : AppCompatActivity()  {
    var cm = intArrayOf(
    -1, 0, 0, 0, 255,
    0, -1, 0, 0, 255,
    0, 0, -1, 0, 255,
    0, 0, 0, 1, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val bitmap = createBitmap(100, 100, Bitmap.Config.RGB_565) // передайте сюда битмап пж, это просто, чтобы было пока
        bitmap?.apply {
            val hei = bitmap.height
            val wi = bitmap.width
            for (i in 0 until wi) {
                for (j in 0 until hei) {
                    val color = bitmap.getPixel(i, j)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)
                    val a = Color.alpha(color)

                    val nr = (r * cm[0] + g * cm[1] + b * cm[2] + a * cm[3] + cm[4])
                    val ng = (r * cm[5] + g * cm[6] + b * cm[7] + a * cm[8] + cm[9])
                    val nb = (r * cm[10] + g * cm[11] + b * cm[12] + a * cm[13] + cm[14])
                    val na = (r * cm[15] + g * cm[16] + b * cm[17] + a * cm[18] + cm[19])

                    bitmap.setPixel(i, j, Color.argb(na, nr, ng, nb))
                    }
                }
            }

        }

}