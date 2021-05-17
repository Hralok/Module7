package com.example.uncleanpower

import android.content.Context
import android.graphics.Bitmap

class Scale { /* ЛАЖА */
    fun sscale(koef: Double, context: Context, originalBitmap: Bitmap?): Bitmap? {
        var bitmap : Bitmap? = originalBitmap
        originalBitmap?.apply {
            val hei = originalBitmap.height
            val wi = originalBitmap.width
            val nh = (hei * koef).toInt()
            val nw = (wi * koef).toInt()
            bitmap = Bitmap.createScaledBitmap(originalBitmap, nw, nh, false)
        }
        return bitmap
    }
}