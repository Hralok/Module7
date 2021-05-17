package com.example.uncleanpower

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_second.*
import java.io.File
import com.example.uncleanpower.FilterInv
import com.example.uncleanpower.ColorCorrection
import com.example.uncleanpower.FilterGW
import com.example.uncleanpower.FiltrPerRef
import com.example.uncleanpower.bottomnavfrag.*
import com.example.uncleanpower.Rotation
import com.example.uncleanpower.StaticColorCorrection
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.uncleanpower.bottomnavfrag.*
import com.example.uncleanpower.databinding.ActivitySecondBinding
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME = "photo.jpg"
        const val CAMERA_SOURCE = 1
        const val GALLERY_SOURSE = 2
        const val CAMERA_REQUEST_CODE = 3
        const val GALLERY_REQUEST_CODE = 4
    }

    private val viewBinding by viewBinding(ActivitySecondBinding::bind, R.id.sec_ac)
    val filtersfrag = FiltersFragment()
    val crrotfrag = CropRotateFragment()
    val drawfrag = DrawFragment()

    var angl:Double = 0.1

    val gwfil = GrayWorldFragment()
    val ccfil = ColorCorrectionFragment()
    val invfil = InversionFragment()

    private lateinit var photoFile: File
    private var takenImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        buttonTap()

        val img_sourse = intent.getIntExtra(MainActivity.imgSourseKey, CAMERA_SOURCE)

        if (img_sourse == CAMERA_SOURCE){
            getCameraBitmap()
        }
        else if (img_sourse == GALLERY_SOURSE) {
            getGalleryBitmap()
        }

        viewBinding.botNav.setOnNavigationItemSelectedListener {item ->
            val trans =  supportFragmentManager.beginTransaction()
            var itid = item.itemId
            when (item.itemId) {
                R.id.effects -> {
                    trans
                        .replace(R.id.all_nav, FiltersFragment.newInstance(), FiltersFragment.TAG)
                        .commit()

                    true
                }

                R.id.draw -> {
                    trans
                        .replace(R.id.all_nav, DrawFragment.newInstance(), DrawFragment.TAG)
                        .commit()
                    true
                }

                R.id.crop_rotate -> {
                    trans
                        .replace(R.id.all_nav, CropRotateFragment.newInstance(), CropRotateFragment.TAG)
                        .commit()
                    true
                }

                R.id.scale -> {
                    trans
                            .replace(R.id.all_nav, ScaleFragment.newInstance(), ScaleFragment.TAG)
                            .commit()
                            dialog()
                    true
                }
                else -> false
            }

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

    private fun getCameraBitmap(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(this, "edu.stanford.rkpandey.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun getGalleryBitmap(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private fun getPhotoFile(fileName: String?): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == SecondActivity.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)



            imageView2.setImageBitmap(takenImage)
        }
        else if (requestCode == SecondActivity.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val imgPath: Uri?  = data?.data
            imageView2.setImageURI(imgPath)

            takenImage = imageView2.drawable.toBitmap()


            val source = BitmapFactory.decodeResource(this.getResources(), R.drawable.rkccehynkiy)

            imageView2.setImageBitmap(StaticColorCorrection().corr(source, takenImage))
//            imageView2.setImageBitmap(Rotation().rotate(takenImage, angl))
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun dialog () {
        var m_Text = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Введите коэффициент масштабирования")
        val input = EditText(this)

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Oк", DialogInterface.OnClickListener {
            dialog, which -> m_Text = input.text.toString()
            scaled(m_Text)
        })
        builder.setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

     fun scaled (k: String) {
         val koef = k.replace (',', '.')
         val regex = "[0-9]+[.]?[0-9]*".toRegex()
         if (regex.matches(koef)) {
             val what = koef.toDouble()
             val negImg = Scale()
             imageView2.setImageBitmap(negImg.sscale(what, this, takenImage))
         } else {
             Toast.makeText(this, "Упс! Вы ввели что-то не то! Попробуйте снова.", Toast.LENGTH_SHORT).show()
         }

    }

    private fun buttonTap() {
        button.setOnClickListener {
            getGalleryBitmap()
        }
        button2.setOnClickListener {
            dialogo()
        }
    }

    private fun dialogo () {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Введите коэффициент масштабирования")
        val input = EditText(this)


        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Oк", DialogInterface.OnClickListener {
            dialog, which -> angl = input.text.toString().toDouble()
        })
        builder.setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()

    }



}