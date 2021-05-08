package com.example.uncleanpower

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat

const val CAMERA_RQ = 101
const val STORAGE_RQ = 102
private lateinit var photoFile: File

class MainActivity : AppCompatActivity() {

    companion object {
        const val imgSourseKey = "imgSourseKey"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        buttonTap()
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
        gal_butt.setOnClickListener {
            if (checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_RQ))
            {
                var intent = Intent(this, SecondActivity::class.java)
                intent.putExtra(imgSourseKey, 1)

                startActivity(intent)
            }
        }
        cam_butt.setOnClickListener {
            if (checkPerm(Manifest.permission.CAMERA, CAMERA_RQ))
            {
                var intent = Intent(this, SecondActivity::class.java)
                intent.putExtra(imgSourseKey, 2)

                startActivity(intent)

//                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//                photoFile = getPhotoFile(FILE_NAME)
//
//
//                val fileProvider = FileProvider.getUriForFile(this, "edu.stanford.rkpandey.fileprovider", photoFile)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
//
//                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
        }
    }

}





