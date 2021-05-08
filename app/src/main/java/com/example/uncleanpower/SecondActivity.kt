package com.example.uncleanpower

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second.*
import java.io.File
import com.example.uncleanpower.FilterInv
import com.example.uncleanpower.ColorCorrection
import com.example.uncleanpower.FilterGW

private lateinit var photoFile: File
private const val FILE_NAME = "photo.jpg"
private var takenImage: Bitmap? = null

class SecondActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)



        val img_sourse = intent.getIntExtra(MainActivity.imgSourseKey, 2)

        if (img_sourse == 2){
            getCameraBitmap()
        }
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

    fun getCameraBitmap() {
        photoFile = getPhotoFile(FILE_NAME)

        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(this, "edu.stanford.rkpandey.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun getPhotoFile(fileName: String?): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == SecondActivity.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

            val negImg = FilterGW()

            imageView2.setImageBitmap(negImg.GrayWorld(takenImage))
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    fun toMenu (view: View) {
//        val menu = Intent(this, MainActivity::class.java)
//        startActivity(menu)
        getCameraBitmap()
    }
}