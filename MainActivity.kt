package com.example.aireader

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var urlInput: EditText
    private lateinit var summaryOutput: TextView
    private val API_KEY = "YOUR_GEMINI_API_KEY_HERE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlInput = findViewById(R.id.urlInput)
        summaryOutput = findViewById(R.id.summaryOutput)

        findViewById<Button>(R.id.summarizeButton).setOnClickListener {
            val url = urlInput.text.toString().trim()
            if (url.isEmpty()) {
                Toast.makeText(this, "Enter a valid link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            summaryOutput.text = "Summarizing..."
            ApiClient.summarize(url, API_KEY) { summary ->
                runOnUiThread { summaryOutput.text = summary }
            }
        }

        // Handle shared URLs
        if (intent?.action == "android.intent.action.SEND" && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                urlInput.setText(it)
            }
        }
    }
}
