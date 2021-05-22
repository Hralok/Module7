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
        var mapbit : Bitmap? = null
        bitmap?.let {
            val hei = bitmap.height
            val wi = bitmap.width

           if (koef < 1) {
               val newwi = (ceil(wi * koef)).toInt()
               val newhei = (ceil(hei*koef)).toInt()
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
                       i++
                       m--
                   } else {
                       x = 0
                       for (j in 0 until wi) {
                           if (n < 1) {
                               if (x < newwi) {
                                   n += wptd
                                   val hey = bitmap.getPixel(j, i)
                                   mapbit?.setPixel(x, y, hey)
                                   x++
                               }
                           } else {
                               n--
                           }
                       }
                       m += wptd
                       y++
                   }
                   i++
               }
           }
            else {
               val newwi = (floor(wi * koef)).toInt()
               val newhei = (floor(hei * koef)).toInt()
               mapbit = Bitmap.createBitmap(newwi, newhei, Bitmap.Config.ARGB_8888)
               var n = koef
               var m = koef
               var x = 0
               var y = 0
               var j = 0

               for (i in 0 until newhei) {
                   x = 0
                   if (m >= 1) {
                       m--
                   }
                   else {
                       m += koef
                       y++
                   }
                   for (j in 0 until newwi) {
                       if (n < 1) {
                       n += koef
                       x++
                   } else {
                       n--
                   }
                       val hey = bitmap.getPixel(x, y)
                       mapbit?.setPixel(j, i, hey)

                   }

                   }
           }
        }
        return mapbit
    }
}