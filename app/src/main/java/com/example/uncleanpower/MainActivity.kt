package com.example.uncleanpower

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

const val CAMERA_RQ = 101
const val STORAGE_RQ = 102

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTap()
    }


    val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) { uri ->
        // используем bitmap
    }

    private fun checkPerm (permission: String, requestCode: Int):Boolean {
        /*
        Проверяет наличие разрешения на использование камеры/галереи, в случае наличия разрешения возвращает значение true,
        в противном случае запрашивает разрешение у пользователя после чего возвращает true или false в зависимости от того, было
        ли получено разрешение
        */
        return if (ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED) {
            true
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun buttonTap() {
//        gal_butt.setOnClickListener {
//            if (checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_RQ))
//            {
//                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            }
//        }
        cam_butt.setOnClickListener {
            if (checkPerm(Manifest.permission.CAMERA, CAMERA_RQ))
            {
                camera.launch()


            }
        }
    }
}

//    val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
//        when {
//            granted -> {
//                camera.launch()// доступ к камере разрешен, открываем камеру
//            }
//            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
//                // доступ к камере запрещен, пользователь поставил галочку Don't ask again.
//            }
//            else -> {
//                // доступ к камере запрещен
//            }
//        }
//    }
