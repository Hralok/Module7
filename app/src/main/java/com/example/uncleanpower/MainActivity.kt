package com.example.uncleanpower

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var photoFile: File

    companion object {
        const val CAMERA_RQ = 101
        const val STORAGE_RQ = 102
        const val CAMERA_SOURCE = 1
        const val STORAGE_SOURSE = 2
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
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra(imgSourseKey, STORAGE_SOURSE) //сделать код источника понятнее

                startActivity(intent)
            }
        }
        cam_butt.setOnClickListener {
            if (checkPerm(Manifest.permission.CAMERA, CAMERA_RQ))
            {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra(imgSourseKey, CAMERA_SOURCE)

                startActivity(intent)
            }
        }
    }

}





