package com.example.algolens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)

        // Find the button by its ID and set a click listener
        val startButton: Button = findViewById(R.id.start_button)

        startButton.setOnClickListener {
            // Start the MainActivity when the button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
