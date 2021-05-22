package com.example.uncleanpower

import android.annotation.SuppressLint
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
import com.example.uncleanpower.JackalRotation
import com.example.uncleanpower.StaticColorCorrection
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.uncleanpower.bottomnavfrag.*
import com.example.uncleanpower.databinding.ActivitySecondBinding
import kotlinx.android.synthetic.main.activity_second.*
import java.lang.Math.sqrt

class SecondActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME = "photo.jpg"
        const val CAMERA_SOURCE = 1
        const val GALLERY_SOURSE = 2
        const val CAMERA_REQUEST_CODE = 3
        const val GALLERY_REQUEST_CODE = 4
    }

    var needColCor:Boolean = false
    var needGW:Boolean = false
    var needInv:Boolean = false
    var needPerRef:Boolean = false
    var needStaticColCor:Boolean = false
    //val source = BitmapFactory.decodeResource(this.getResources(), R.drawable.rkccehynkiy)
    var needBlur:Boolean = false
    var blurKoef:Int = 1
    var needScale:Boolean = false
    var scaleKoef:Double = 1.0
    var needRotate:Boolean = false
    var angl:Double = 0.0

    var toOriginal:Boolean = true

    private lateinit var photoFile: File
    private var takenImage: Bitmap? = null
    private var renderedImage: Bitmap? = null

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

        fragments()
    }

    private fun fragments(){
        bot_nav.setOnNavigationItemSelectedListener {item ->

            when (item.itemId) {
                R.id.effects -> {

                    true
                }

                R.id.crop_rotate -> {
                    dialogForRotate()
                    true
                }

                R.id.scale -> {
                    if (needScale){
                        needScale = false
                    }
                    else
                    {
                        needScale = true
                        dialogForScale()
                    }
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
        filterReset()

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(this, "edu.stanford.rkpandey.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun getGalleryBitmap(){
        filterReset()

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

    private fun filterReset(){
        needColCor = false
        needGW = false
        needInv = false
        needPerRef = false
        needStaticColCor = false
        needBlur = false
        blurKoef = 1
        needScale = false
        scaleKoef = 1.0
        needRotate = false
        angl = 0.1
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

        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun dialogForScale () {
        var m_Text = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Введите коэффициент масштабирования")
        val input = EditText(this)

        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(scaleKoef.toString())
        builder.setView(input)

        builder.setPositiveButton("Oк", DialogInterface.OnClickListener {
            dialog, _ -> m_Text = input.text.toString()
            val koef = m_Text.replace (',', '.')
            val regex = "[0-9]+[.]?[0-9]*".toRegex()

            if (regex.matches(koef)) {
                scaleKoef = koef.toDouble()
                dialog.cancel()
            }
            else {
                Toast.makeText(this, "Упс! Вы ввели что-то не то! Попробуйте снова.", Toast.LENGTH_LONG).show()
                dialogForScale ()
            }
        })

        builder.show()
    }

    private fun dialogForNewImage () {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите источник изображения")

        builder.setPositiveButton("Галлерея", DialogInterface.OnClickListener { dialog, _ -> getGalleryBitmap(); dialog.cancel()})
        builder.setNegativeButton("Камера", DialogInterface.OnClickListener { dialog, _ -> getCameraBitmap(); dialog.cancel() })

        builder.show()
    }

    private fun dialogForSize () {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Вы хоите применить изменения к оригинальному изображению или к его уменьшенной версии? " +
                "Применение к уменьшенной версии позволит выполнить алгоритмы быстрее!")

        builder.setPositiveButton("Применить к оригинальному", DialogInterface.OnClickListener { dialog, _ -> toOriginal = true; render(); dialog.cancel()})
        builder.setNegativeButton("Применить к уменьшеному", DialogInterface.OnClickListener { dialog, _ -> toOriginal = false; render(); dialog.cancel() })

        builder.show()
    }

    private fun dialogForRotate(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setPositiveButton("Добавить угол к текущему", DialogInterface.OnClickListener { dialog, _ -> dialogForRotatePlus(); dialog.cancel()})
        builder.setNegativeButton("Задать угол", DialogInterface.OnClickListener { dialog, _ -> ; dialog.cancel() })

        builder.show()
    }

    private fun dialogForRotatePlus() {
        var m_Text = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Текущий угол поворота: $angl")

        val input = EditText(this)

        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(scaleKoef.toString())
        builder.setView(input)

        builder.setNegativeButton("Добавить", DialogInterface.OnClickListener { dialog, _ -> m_Text = input.text.toString()
            val koef = m_Text.replace (',', '.')
            val regex = "[0-9]+[.]?[0-9]*".toRegex()

            if (regex.matches(koef)) {
                angl += koef.toDouble()
                dialog.cancel()
            }
            else {
                Toast.makeText(this, "Упс! Вы ввели что-то не то! Попробуйте снова.", Toast.LENGTH_LONG).show()
                dialogForRotatePlus()
            } })

        builder.show()
    }


    private fun buttonTap() {
        button.setOnClickListener {
            dialogForNewImage()
        }
        button2.setOnClickListener {
            dialogForSize()
        }
    }

    private fun convertToRealSize(original:Bitmap?):Bitmap? {
        var corrected:Bitmap? = null

        original?.let {
            if (original.height * original.width > 250000){
                val scKoef = sqrt(250000 / (original.height * original.width).toDouble())
                corrected = Scale().sscale(scKoef, original)
            }
            else{
                corrected = original
            }
        }

        return corrected
    }

    private fun render () {
        needRotate = angl != 0.0

        if (toOriginal){
            renderedImage = takenImage
        }
        else{
            renderedImage = convertToRealSize(takenImage)
        }

        if (needColCor){
            renderedImage = ColorCorrection().corr(renderedImage)
            Toast.makeText(this, "ColorCorrection completed", Toast.LENGTH_SHORT).show()
        }
        if (needPerRef){
            renderedImage = FiltrPerRef().PerfectReflector(renderedImage)
            Toast.makeText(this, "PerfectReflection completed", Toast.LENGTH_SHORT).show()
        }
        if (needStaticColCor){
            //renderedImage = StaticColorCorrection().corr(source, renderedImage)
            Toast.makeText(this, "StaticColorCorrection completed", Toast.LENGTH_SHORT).show()
        }
        if (needGW){
            renderedImage = FilterGW().GrayWorld(renderedImage)
            Toast.makeText(this, "GrayWorld completed", Toast.LENGTH_SHORT).show()
        }
        if (needInv){
            renderedImage = FilterInv().transform(renderedImage)
            Toast.makeText(this, "Inversion completed", Toast.LENGTH_SHORT).show()
        }
        if (needBlur){
            renderedImage = GaussianBlur().gauss(renderedImage, blurKoef)
            Toast.makeText(this, "Blur completed", Toast.LENGTH_SHORT).show()
        }
        if (needScale){
            renderedImage = Scale().sscale(scaleKoef, renderedImage)
            Toast.makeText(this, "Scale completed", Toast.LENGTH_SHORT).show()
        }
        if (needRotate){
            renderedImage = JackalRotation().rotate(renderedImage, angl)
            Toast.makeText(this, "Rotation completed", Toast.LENGTH_SHORT).show()
        }

        imageView2.setImageBitmap(renderedImage)
    }





}