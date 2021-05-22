package com.example.uncleanpower

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import java.lang.Math.ceil
import java.lang.Math.floor

class Scale {

    fun sscale(koef: Double, originalBitmap: Bitmap?): Bitmap? {
        val bitmap : Bitmap? = originalBitmap
        var mapbit : Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        bitmap?.let {
            val hei = bitmap.height
            val wi = bitmap.width

           if (koef < 1) {
               val newwi = (ceil(wi * koef)).toInt()
               val newhei = (ceil(hei * koef)).toInt()
               val negk = 1 - koef
               mapbit = Bitmap.createBitmap(newwi, newhei, Bitmap.Config.ARGB_8888)
               val wptd = negk/koef
               var x = 0
               var y = 0
               var m = 0.0
               var n = 0.0
               var i = 0

               while (i < hei) {
                   if (m >= 1) {
                       m -= 1
                   }
                   else {
                       m += wptd
                       x = 0
                       for (j in 0 until wi) {
                           if (n < 1) {
                               if (x < newwi) {
                                   n += wptd
                                   val hey = bitmap.getPixel(j, i)
                                   mapbit.setPixel(x, y, hey)
                                   x++
                               }
                           } else {
                               n -= 1
                           }
                       }
                       y++
                   }
                   i++
               }
           }
            else {
                val newwi = (floor(wi * koef)).toInt()
                val newhei = (floor(hei * koef)).toInt()
                mapbit = Bitmap.createBitmap(newwi, newhei, Bitmap.Config.ARGB_8888)
                var r = koef
                var m = 0.0
                var x = 0
                var y = 0

                for (j in 0 until newhei) {
                    if (m < 1) {
                        y = if (y < bitmap.height - 1) y+1 else bitmap.height - 1
                        m += koef - 1
                        r = koef
                        x = 0
                        for (i in 0 until newwi) {
                            if (r >= 1) {
                                mapbit.setPixel(i, j, bitmap.getPixel(x, y))
                                r -= 1
                            } else {
                                x++
                                r += koef-1
                                mapbit.setPixel(i, j, bitmap.getPixel(x, y))
                            }
                        }
                    }
                    else{
                        for (i in 0 until newwi) {
                            mapbit.setPixel(i, j, mapbit.getPixel(i, j - 1))
                        }
                        m -= 1
                    }
                }
            }
        }
        return mapbit
    }
}