package com.chesire.passpusher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.passpusher.databinding.ActivityMainBinding

/**
 * The main activity for the application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
