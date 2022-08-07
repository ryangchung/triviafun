/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * This is the main activity for the app.
 */

package com.ryan.trivia_app.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ryan.trivia_app.R
import com.ryan.trivia_app.controller.Transfer
import com.ryan.trivia_app.controller.UIController
import com.ryan.trivia_app.databinding.ActivityMainBinding

/** MainActivity class. All menus will run on top of this activity. */
class MainActivity : AppCompatActivity() {
    /**
     * When the activity is created on screen.
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.app)
        setContentView(binding.root)

        // System theme gets disregarded
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //  Error message Toast if there is an internet connection error inside the other activity.
        val errorMessage = intent.extras?.getString("error")
        if (errorMessage != null) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        // Replaces the placeholder with the main menu
        Transfer().transferToFragment(
            supportFragmentManager, R.id.fragmentPlaceholder, MenuFragment()
        )
    }

    /** When the app is resumed. */
    override fun onResume() {
        super.onResume()

        // Makes the app fullscreen (No system bars)
        UIController().fullScreen(window)
    }
}
