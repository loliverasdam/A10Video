package com.example.fileprovider

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.MediaController
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.viewbinding.BuildConfig
import com.example.fileprovider.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnTakeVideo.setOnClickListener()
        {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).also{
                it.resolveActivity(packageManager).also{component->
                    createVideoFile()

                    val videoUri: Uri = FileProvider.getUriForFile(this,"com.example.fileprovider.fileprovider", file)

                    it.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                }
            }
            startForResult.launch(intent)
        }
    }

    private lateinit var file:File
    private fun createVideoFile() {
        val dir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)

        file = File.createTempFile("IMG_${System.currentTimeMillis()}_",".mp4", dir)
        Log.i("ruta", file.toString())
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        val mediaController = MediaController(this)
        val videoView = binding.miniatureVideo

        videoView.setVideoURI(Uri.parse(file.toString()))
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.start()
    }
}