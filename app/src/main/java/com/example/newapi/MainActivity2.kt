package com.example.newapi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.newapi.databinding.ActivityMain2Binding
import java.util.*

class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val heading = intent.getStringExtra("heading")
        val imageUrl = intent.getStringExtra("imageUrl")
        val url = intent.getStringExtra("url")
        val description = intent.getStringExtra("description")

        checkTextToSpeechAvailability()

        if (heading!!.isNotEmpty() && description!!.isNotEmpty()) {

            binding.titleTextView.setText(heading)
            binding.descriptionTextView.setText(description)

            Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .into(binding.imageView)

        }

        binding.desBtn.setOnClickListener {

            binding.desBtn.visibility = View.GONE
            binding.webBtn.visibility = View.GONE

            binding.layoutTv.visibility = View.VISIBLE

        }

        binding.webBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }

        binding.speakIcon.setOnClickListener {

            textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)

        }

    }

    private fun checkTextToSpeechAvailability() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {

                val result = textToSpeech.setLanguage(Locale.US)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language does not support!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to initialize text to speech", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}