package com.example.uncleanpower

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.activity.result.launch
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.cameraButton
import kotlinx.android.synthetic.main.activity_camera.closeButton
import kotlinx.android.synthetic.main.activity_camera.imageContainer
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraActivity : AppCompatActivity() {
    val permission = registerForActivityResult(RequestPermission()) { granted ->
        when {
            granted -> {
                camera.launch() // доступ к камере разрешен, открываем камеру
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // доступ к камере запрещен, пользователь поставил галочку Don't ask again.
            }
            else -> {
                // доступ к камере запрещен
            }
        }
    }

    val camera = registerForActivityResult(TakePicturePreview()) { bitmap ->
        // используем bitmap
    }

//    val custom = registerForActivityResult(MySecondActivityContract()) { result ->
//        // используем result
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraButton.setOnClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // объясняем пользователю, почему нам необходимо данное разрешение
            } else {
                permission.launch(Manifest.permission.CAMERA)
            }
        }

//        vButtonSecondActivity.setOnClickListener {
//            custom.launch("What is the answer?")
//        }
    }



//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        cameraButton.setOnClickListener {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                // we need to tell user why do we need permission
////                showRationaleDialog()
//            } else {
//                cameraPermission.launch(Manifest.permission.CAMERA)
//            }
//        }
//
//        closeButton.setOnClickListener {
//            setImageIsVisible(false)
//        }
//
//        setFragmentResultListener(RATIONALE_KEY) { _, bundle ->
//            val isWantToAllowAfterRationale = bundle.getBoolean(RESULT_KEY)
//            if (isWantToAllowAfterRationale) {
//                cameraPermission.launch(Manifest.permission.CAMERA)
//            }
//        }
//        setFragmentResultListener(SETTINGS_KEY) { _, bundle ->
//            val isWantToOpenSettings = bundle.getBoolean(RESULT_KEY)
//            if (isWantToOpenSettings) {
//                openSettings()
//            }
//        }
//    }
//
//    private fun showToast(textId: Int) {
//        Toast.makeText(context, textId, Toast.LENGTH_SHORT).show()
//    }
//
////    private fun showRationaleDialog() {
////        RationaleFragment().show(parentFragmentManager, RationaleFragment.TAG)
////    }
////
////    private fun showSettingsDialog() {
////        DontAskAgainFragment().show(parentFragmentManager, DontAskAgainFragment.TAG)
////    }
//
//    private fun openSettings() {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                .setData(Uri.fromParts("package", requireContext().packageName, null))
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//    }
//
//    private fun setImageIsVisible(isVisible: Boolean) {
//        cameraButton.isVisible = !isVisible
//        closeButton.isVisible = isVisible
//        imageContainer.isVisible = isVisible
//    }
//
//    companion object {
//        const val RATIONALE_KEY = "rationale_tag"
//        const val SETTINGS_KEY = "settings_tag"
//        const val RESULT_KEY = "camera_result_key"
//    }

}