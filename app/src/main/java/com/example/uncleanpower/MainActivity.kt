package com.example.uncleanpower

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

val CAMERA_RQ = 101
val STORAGE_RQ = 102

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonTap()
    }

    private fun checkPerm (permission: String, requestCode: Int) {
        when {ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
        //shouldShowRequestPermissionRationale(permission)
        else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
        }

    private fun buttonTap() {
        gal_butt.setOnClickListener {
            checkPerm(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_RQ)
        }
        cam_butt.setOnClickListener {
            checkPerm(Manifest.permission.CAMERA, CAMERA_RQ)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun secondScr (view: View) {
        val secondS = Intent(this, SecondActivity::class.java)
        startActivity(secondS)
    }
}