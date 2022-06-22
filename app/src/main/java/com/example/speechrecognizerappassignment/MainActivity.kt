package com.example.speechrecognizerappassignment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.example.speechrecognizerappassignment.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var answer:String?=null
    private val question= "What is 2 multiplied by 2"
    private val requiredAnswer = "Four"
    private var userAnswer=false


    companion object {
        private const val REQUEST_CODE_STT = 1
    }

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.UK
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(userAnswer){
            if(answer==requiredAnswer){
                textToSpeech.speak("Right answer", TextToSpeech.QUEUE_FLUSH, null, null)
            }
            else{
                textToSpeech.speak("Wrong answer", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        binding.btnSpeak.setOnClickListener {
            val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start speaking...")

            try {
                startActivityForResult(speechRecognizerIntent, REQUEST_CODE_STT)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }


        binding.btnListen.setOnClickListener {
            textToSpeech.speak(question, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== REQUEST_CODE_STT && resultCode==Activity.RESULT_OK && data!=null){
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result?.let {
                answer=it[0]
                userAnswer=true
            }
        }
    }

    override fun onPause() {
        textToSpeech.stop()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeech.shutdown()
        super.onDestroy()
    }
}