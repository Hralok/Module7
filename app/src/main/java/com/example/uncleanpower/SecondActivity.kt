package com.example.uncleanpower

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)



        var imageUri = intent.data



    }

    fun toMenu (view: View) {
        val menu = Intent(this, MainActivity::class.java)
        startActivity(menu)
    }
}