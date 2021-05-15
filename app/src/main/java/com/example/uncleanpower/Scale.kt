package com.example.uncleanpower

import android.graphics.Bitmap

class Scale {
    fun sscale(koef: Float, originalBitmap: Bitmap?): Bitmap? {
        var bitmap = originalBitmap?.copy(Bitmap.Config.ARGB_8888, true)
        originalBitmap?.apply {
            val hei = originalBitmap.height
            val wi = originalBitmap.width
            val nh = (hei * koef).toInt()
            val nw = (wi * koef).toInt()
            bitmap = Bitmap.createScaledBitmap(originalBitmap, nw, nh, true)
        }
        return bitmap
    }
}