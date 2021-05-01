package com.example.uncleanpower

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


private const val PERMISSION_REQUEST_CODE = 123

class PermGalleryActivity : AppCompatActivity(R.layout.activity_main) {
    private fun requestPerms() {
        val permissions = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

}
//        val singlePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//            when {
//                granted -> {
//                    //запускать галерею
//                }
//                else -> {
//                // пользователь отклонил запрос
//                }
//            }
//    }

//}